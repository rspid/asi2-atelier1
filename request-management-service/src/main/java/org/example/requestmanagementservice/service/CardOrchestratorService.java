package org.example.requestmanagementservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardOrchestratorService {

    @Autowired
    private CardRequestRepository cardRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Champs de classe pour stocker les valeurs
    private UUID cardRequestId;

    public CardRequest createCardRequest() {
        // Créer une nouvelle demande de carte et définir son statut initial
        CardRequest cardRequest = new CardRequest();
        cardRequest.setStatus("PROCESSING");

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
            // Débiter l'utilisateur
            boolean debitSuccess = debitUser();
            if (!debitSuccess) {
                updateCardStatus(cardRequestId, "FAILED");
                return;
            }

            // URL du service de génération de texte
            String textServiceUrl = "http://localhost:8083/api/text";

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
            cardRequest.setPromptText(description);
            cardRequest.setStatus("TEXT_RECEIVED");
            cardRequestRepository.save(cardRequest);

            // Construire le corps de la requête pour l'API d'image
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestId", cardRequestId.toString());
            requestBody.put("text", description);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            // Appeler l'API d'image
            String imageServiceUrl = "http://localhost:8081/api/image";
            try {
                System.out.println("Calling image service with JSON body: " + requestBody);
                ResponseEntity<String> response = restTemplate.exchange(
                        imageServiceUrl,
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

    public void updateImage(UUID cardRequestId, String imageUrl) {
        // Mettre à jour l'URL de l'image dans la base de données et marquer la demande
        // comme "COMPLETED"
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setPromptImage(imageUrl);
            cardRequest.setStatus("COMPLETED");
            cardRequestRepository.save(cardRequest);
        }
    }

    private boolean debitUser() {
        // Simuler le débit de l'utilisateur
        return true;
    }

    public void updateCardStatus(UUID cardRequestId, String status) {
        Optional<CardRequest> optionalCardRequest = cardRequestRepository.findById(cardRequestId);
        if (optionalCardRequest.isPresent()) {
            CardRequest cardRequest = optionalCardRequest.get();
            cardRequest.setStatus(status);
            cardRequestRepository.save(cardRequest);
        }
    }
}
