package org.example.propertycalculationservice.service;

import org.example.propertycalculationservice.model.ImagePropertiesRequest;
import org.example.propertycalculationservice.model.ImagePropertiesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tp.cpe.ImgToProperties;

import java.util.Map;

@Service

public class MessageConsumerService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${orchestrator.url}")
    private String orchestratorUrl;

    public MessageConsumerService(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().build();
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "imagePropertiesQueue")
    public void consumeMessage(String messageJson) {
        try {
            ImagePropertiesRequest request = objectMapper.readValue(messageJson, ImagePropertiesRequest.class);

            // Utiliser la librairie pour obtenir les propriétés de l'image
            Map<String, Float> properties = ImgToProperties.getPropertiesFromImg(
                    request.getImageUrl(),
                    100f,    // valueToDispatch
                    4,       // nb_of_colors
                    0.3f,    // randPart
                    false     // isBase64?
            );

            // Créer la réponse
            ImagePropertiesResponse response = new ImagePropertiesResponse(request.getRequestId(), properties);

            // Envoyer le résultat à l'orchestrateur
            sendToOrchestrator(response);

        } catch (Exception e) {
            // Gérer l'exception de manière appropriée
            e.printStackTrace();
        }
    }

    private void sendToOrchestrator(ImagePropertiesResponse response) {
        try {
            webClient.post()
                    .uri(orchestratorUrl + "/api/cards/reponse/properties")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(response)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            // Gérer l'exception de manière appropriée
            e.printStackTrace();
        }
    }
}