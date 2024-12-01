package org.example.requestmanagementservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.requestmanagementservice.dto.CardRequestDto;
import org.example.requestmanagementservice.entity.CardRequest;
import org.example.requestmanagementservice.repository.CardRequestRepository;
import org.example.requestmanagementservice.model.GenerationRequest;
import org.example.requestmanagementservice.model.GenerationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CardOrchestratorService {

    @Autowired
    private CardRequestRepository cardRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Champs de classe pour stocker les valeurs
    private UUID cardRequestId;

    @Value("${text.generation.service.url}")
    private String textServiceUrl;

    @Value("${image.generation.service.url}")
    private String imageServiceUrl;

    @Value("${property.calculation.service.url}")
    private String propertyServiceUrl;

    @Value("${backend.service.url}")
    private String backendServiceUrl;

    @Value("${gateway.service.url}")
    private String gatewayServiceUrl;

    public CardRequest createCardRequest(String userId, String prompt) {
        // Créer une nouvelle demande de carte et définir son statut initial
        CardRequest cardRequest = new CardRequest();
        cardRequest.setStatus("PROCESSING");
        cardRequest.setUserId(userId);

        // Enregistrer la demande dans la base de données pour générer un ID
        cardRequest = cardRequestRepository.save(cardRequest);

        // Initialiser cardRequestId et requestId dans les champs de classe
        this.cardRequestId = cardRequest.getId();
        System.out.println("Card request ID: " + cardRequestId);

        // Lancer le traitement asynchrone de la demande
        new Thread(this::processCardRequest).start();

        return cardRequest;
    }

    private void processCardRequest() {
        try {
            Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
            if (optionalCardRequest.isPresent()) {
                CardRequest cardRequest = optionalCardRequest.get();
                if (!debitUser(cardRequest.getUserId())) {
                    updateCardStatus(cardRequestId, "INSUFFICIENT_FUNDS");
                    return;
                }
            } else {
                updateCardStatus(cardRequestId, "USER_NOT_FOUND");
                return;
            }

            // URL du service de génération de texte
            String textServiceUrl = "http://text-generation-service/api/text";

            // Préparer la requête de génération de texte
            GenerationRequest generationRequest = new GenerationRequest();
            generationRequest.setRequestId(cardRequestId.toString());
            generationRequest.setPrompt("Initial text prompt");

            // Préparer les en-têtes pour spécifier le type de contenu
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construire l'objet HttpEntity avec les en-têtes et la requête de génération
            HttpEntity<GenerationRequest> requestEntity = new HttpEntity<>(generationRequest, headers);

            // Envoyer la requête au service de texte et capturer la réponse brute
            ResponseEntity<String> response = restTemplate.postForEntity(textServiceUrl, requestEntity, String.class);
            String rawResponse = response.getBody();
            System.out.println("Raw response from text service: " + rawResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTextAndTriggerImageRequest(UUID cardRequestId, String description) {
        // Rechercher l'objet CardRequest avec l'identifiant UUID
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        System.out.println("\n\n\nupdateTextAndTriggerImageRequest started : " + cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setPromptText(extractValue(description, "response"));
            cardRequest.setStatus("TEXT_RECEIVED");
            cardRequestRepository.save(cardRequest);

            // Construire le corps de la requête pour l'API d'image
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestId", cardRequestId.toString());
            requestBody.put("text", description);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // Utiliser l'URL injectée
            String imageGenEndpoint = imageServiceUrl + "/api/image";
            try {
                System.out.println("Calling image service with JSON body: " + requestBody);
                ResponseEntity<String> response = restTemplate.exchange(
                        imageGenEndpoint,
                        HttpMethod.POST,
                        request,
                        String.class);
            } catch (Exception e) {
                System.err.println("Error calling image service: " + e.getMessage());
            }
            System.out.println("updateTextAndTriggerImageRequest completed : " + cardRequestId + "\n\n\n\n");
        } else {
            System.out.println("CardRequest with ID " + cardRequestId + " not found.");
        }
    }

    public void updateImageAndTriggerPropertiesRequest(UUID cardRequestId, String imageUrl) {
        // Mettre à jour l'URL de l'image dans la base de données et marquer la demande
        // comme "COMPLETED"
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setPromptImage(extractValue(imageUrl, "url"));
            cardRequest.setStatus("IMAGE_RECEIVED");
            cardRequestRepository.save(cardRequest);

            // Construire le corps de la requête pour l'API d'image
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestId", cardRequestId.toString());
            requestBody.put("imageUrl", extractAndTransformUrl(imageUrl));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // Utiliser l'URL injectée
            String propertyEndpoint = propertyServiceUrl + "/api/properties";
            try {
                System.out.println("Calling image service with JSON body: " + requestBody);
                ResponseEntity<String> response = restTemplate.exchange(
                        propertyEndpoint,
                        HttpMethod.POST,
                        request,
                        String.class);
            } catch (Exception e) {
                System.err.println("Error calling property service: " + e.getMessage());
            }
            System.out.println("updateImageAndTriggerPropertiesRequest completed : " + cardRequestId + "\n\n\n\n");
        }

    }

    public void updateProperties(UUID cardRequestId, Map<String, Float> properties) {
        Float defense = properties.get("DEFENSE");
        Float energy = properties.get("ENERGY");
        Float hp = properties.get("HP");
        Float attack = properties.get("ATTACK");
        // Mettre à jour les propriétés de la carte dans la base de données
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setAttack(attack);
            cardRequest.setDefense(defense);
            cardRequest.setEnergy(energy);
            cardRequest.setHp(hp);
            cardRequest.setStatus("COMPLETED");
            cardRequestRepository.save(cardRequest);
            System.out.println("Properties updated for card request ID: " + cardRequestId);
        }
    }

    private boolean debitUser(String userId) {
        // Utiliser l'URL injectée
        String debitEndpoint = backendServiceUrl + "/user/" + userId + "/debit";
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    debitEndpoint,
                    HttpMethod.POST,
                    null,
                    Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
            return false;
        }
    }

    public void updateCardStatus(UUID cardRequestId, String status) {
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setStatus(status);
            cardRequestRepository.save(cardRequest);

        }
    }

    public void saveCardRequest(CardRequest cardRequest) {
        CardRequestDto cardRequestDto = new CardRequestDto();
        cardRequestDto.setAttack(cardRequest.getAttack());
        cardRequestDto.setDefence(cardRequest.getDefense());
        cardRequestDto.setEnergy(cardRequest.getEnergy());
        cardRequestDto.setHp(cardRequest.getHp());
        cardRequestDto.setPrice(10);
        cardRequestDto.setUserId(Integer.parseInt(cardRequest.getUserId()));

        try {
            // Utiliser l'URL injectée
            String cardEndpoint = backendServiceUrl + "/card";
            ResponseEntity<CardRequestDto> response = restTemplate.exchange(
                    cardEndpoint,
                    HttpMethod.POST,
                    requestEntity,
                    CardRequestDto.class);

            // Traiter la réponse
            CardRequestDto responseBody = response.getBody();
            System.out.println("Card added successfully: " + responseBody);
        } catch (Exception e) {
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }

    }

    public static String extractAndTransformUrl(String jsonString) {
        // Expression régulière pour extraire la valeur de "url"
        String regex = "\"url\"\\s*:\\s*\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonString);

        if (matcher.find()) {
            String extractedUrl = matcher.group(1); // Extrait "/static/imgs/default-2.jpg"

            // Supprimer le préfixe "/static" si présent
            String cleanedPath = extractedUrl.replaceFirst("^/static/", "");

            // Ajouter "localhost:8080/" devant le chemin nettoyé
            String finalUrl = gatewayServiceUrl + cleanedPath;

            return finalUrl;
        } else {
            // Si "url" n'est pas trouvé dans la chaîne JSON
            System.err.println("Aucune correspondance trouvée pour 'url' dans la chaîne JSON.");
            return null;
        }
    }

    public static String extractValue(String jsonString, String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode valueNode = rootNode.path(key);
            if (!valueNode.isMissingNode() && !valueNode.isNull()) {
                return valueNode.asText();
            } else {
                System.err.println("La clé \"" + key + "\" est absente ou a une valeur null dans la chaîne JSON.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'analyse de la chaîne JSON : " + e.getMessage());
            return null;
        }
    }
}
