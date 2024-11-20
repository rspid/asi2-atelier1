package org.example.textgenerationservice.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.textgenerationservice.model.GenerationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MessageConsumerService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${orchestrator.url}")
    private String orchestratorUrl;

    @Value("${ollama.api.base-url}")
    private String ollamaApiBaseUrl;

    public MessageConsumerService(ObjectMapper objectMapper, @Value("${ollama.api.base-url}") String ollamaApiBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(ollamaApiBaseUrl).build(); // Utilisation de l'URL configurable
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "textGenerationQueue")
    public void consumeMessage(String messageJson) {
        try {
            GenerationRequest request = objectMapper.readValue(messageJson, GenerationRequest.class);
            System.out.println("Received message: " + request);
            // Appel au service Ollama pour générer le texte
            String generatedText = webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new OllamaPromptRequest("qwen2:0.5b", request.getPrompt()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("Generated Text: " + generatedText);

            sendToOrchestrator(request.getRequestId(), generatedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToOrchestrator(String cardRequestId, String generatedText) {
        try {
            // Utilisation de ObjectMapper pour afficher le JSON avant l'envoi
            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(new OrchestratorRequest(cardRequestId, generatedText));
            System.out.println("JSON request being sent to orchestrator: " + jsonRequest);

            System.out.println("About to send POST request to orchestrator with cardRequestId: " + cardRequestId);

            // Envoi de la requête avec le texte extrait dans OrchestratorRequest
            webClient.post()
                    .uri(orchestratorUrl + "/api/cards/response/text")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new OrchestratorRequest(cardRequestId, generatedText))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnSubscribe(subscription -> System.out.println("Request sent to orchestrator"))
                    .doOnError(error -> System.err.println("Error during request: " + error.getMessage()))
                    .doOnSuccess(response -> System.out.println("Request completed successfully"))
                    .block();

        } catch (JsonProcessingException e) {
            System.err.println("Error serializing JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error while processing generated text: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class OrchestratorRequest {
        @JsonProperty("cardRequestId")
        private String cardRequestId;

        @JsonProperty("generatedText")
        private String description;

        // Constructeurs, getters et setters
        public OrchestratorRequest(String cardRequestId, String description) {
            this.cardRequestId = cardRequestId;
            this.description = description;
        }

        public OrchestratorRequest(long long1, String generatedText) {
            // TODO Auto-generated constructor stub
        }
    }

    private static class OllamaPromptRequest {
        @JsonProperty("model")
        private String model;

        @JsonProperty("prompt")
        private String prompt;

        @JsonProperty("stream")
        private boolean stream;

        public OllamaPromptRequest() {
        }

        public OllamaPromptRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
            this.stream = false;
        }
    }
}
