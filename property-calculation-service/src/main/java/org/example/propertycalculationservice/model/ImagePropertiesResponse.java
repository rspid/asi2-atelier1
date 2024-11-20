package org.example.propertycalculationservice.model;

import java.util.Map;

public class ImagePropertiesResponse {
    private String requestId;
    private Map<String, Float> properties;

    public ImagePropertiesResponse(String requestId, Map<String, Float> properties) {
        this.requestId = requestId;
        this.properties = properties;
    }

    // Getters et Setters
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
}