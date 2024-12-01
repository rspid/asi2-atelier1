package org.example.requestmanagementservice.model;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrchestratorRequest {
    @JsonProperty("cardRequestId")
    private String cardRequestId;

    @JsonProperty("generatedText")
    private String generatedText;

    @JsonProperty("generatedImage")
    private String generatedImage;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("properties")
    private Map<String, Float> properties;

    @JsonProperty("prompt")
    private String prompt;


    // constructeur vide
    public OrchestratorRequest() {
    }

    // Constructeurs, getters et setters
    public OrchestratorRequest(String cardRequestId, String generatedText, String generatedImage, String requestId,
            String properties, String prompt) {
        this.cardRequestId = cardRequestId;
        this.generatedText = generatedText;
    }

    public String getCardRequestId() {
        return cardRequestId;
    }

    public void setCardRequestId(String cardRequestId) {
        this.cardRequestId = cardRequestId;
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    public String getgeneratedImage() {
        return generatedImage;
    }

    public void setgeneratedImage(String generatedImage) {
        this.generatedImage = generatedImage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, Float> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Float> properties) {
        this.properties = properties;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
