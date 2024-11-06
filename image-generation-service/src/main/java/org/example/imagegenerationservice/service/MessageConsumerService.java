package org.example.imagegenerationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.imagegenerationservice.model.ImageGenerationRequest;
import org.example.imagegenerationservice.model.NeuralLovePromptRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MessageConsumerService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${orchestrator.url}")
    private String orchestratorUrl;

    @Value("${neural.love.api.url}")
    private String neuralLoveApiUrl;

    public MessageConsumerService(ObjectMapper objectMapper, @Value("${neural.love.api.url}") String neuralLoveApiUrl) {
        this.webClient = WebClient.builder().baseUrl(neuralLoveApiUrl).build();
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "imageGenerationQueue")
    public void consumeMessage(String messageJson) {
        try {
            // Désérialiser la requête
            ImageGenerationRequest request = objectMapper.readValue(messageJson, ImageGenerationRequest.class);

            NeuralLovePromptRequest promptRequest = new NeuralLovePromptRequest(request.getPrompt());

            // Journaliser la requête
            System.out.println("Sending to Neural Love: " + objectMapper.writeValueAsString(promptRequest));

            // Appel à Neural Love pour générer l'image
            String generatedImage = webClient.post()
                    .uri("/fake/prompt/req")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(promptRequest)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("Neural Love API Error: " + errorBody);
                                    return Mono.error(new RuntimeException("Neural Love API Error: " + errorBody));
                                });
                    })
                    .bodyToMono(String.class)
                    .block();

            // Affichage du résultat
            System.out.println("Generated Image: " + generatedImage);

            // Envoi à l'orchestrateur
            sendToOrchestrator(request.getRequestId(), generatedImage);

        } catch (Exception e) {
            System.err.println("Erreur lors de la consommation du message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendToOrchestrator(String requestId, String generatedImage) {
        webClient.post()
                .uri(orchestratorUrl + "/api/v1/receive-generated-image")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new OrchestratorRequest(requestId, generatedImage))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    // Classe interne pour la requête à l'orchestrateur
    private static class OrchestratorRequest {
        private String requestId;
        private String generatedImage;

        public OrchestratorRequest(String requestId, String generatedImage) {
            this.requestId = requestId;
            this.generatedImage = generatedImage;
        }

        // Getters et Setters
        public String getRequestId() {
            return requestId;
        }

        public String getGeneratedImage() {
            return generatedImage;
        }
    }
}
