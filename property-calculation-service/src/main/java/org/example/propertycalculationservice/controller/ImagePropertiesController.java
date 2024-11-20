package org.example.propertycalculationservice.controller;

import org.example.propertycalculationservice.model.ImagePropertiesRequest;
import org.example.propertycalculationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
public class ImagePropertiesController {

    @Autowired
    private MessageQueueService messageQueueService;

    @PostMapping
    public ResponseEntity<String> receiveImagePropertiesRequest(@Validated @RequestBody ImagePropertiesRequest request) {
        // Envoie la demande dans ActiveMQ
        messageQueueService.sendMessageToQueue(request);
        return ResponseEntity.accepted().body("Request received with ID: " + request.getRequestId());
    }
}