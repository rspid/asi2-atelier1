package org.example.propertycalculationservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class ImagePropertiesRequest implements Serializable {
    @NotNull
    private String requestId;

    @NotEmpty
    private String imageUrl;

    // Getters et Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}