package org.example.requestmanagementservice.controller;

import org.example.requestmanagementservice.entity.CardRequest;
import org.example.requestmanagementservice.model.GenerationResponse;
import org.example.requestmanagementservice.model.OrchestratorRequest;
import org.example.requestmanagementservice.service.CardOrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
public class CardOrchestratorController {

    @Autowired
    private CardOrchestratorService cardOrchestratorService;

    @PostMapping("/create")
    public ResponseEntity<CardRequest> createCard() {
        // Appelle le service pour gérer la création de la carte
        CardRequest cardRequest = cardOrchestratorService.createCardRequest();
        return ResponseEntity.ok(cardRequest); // Retourne la réponse initiale
    }

    @PostMapping("/response/text")
    public ResponseEntity<String> receiveTextResponse(@RequestBody OrchestratorRequest request) {
        if (request.getGeneratedText() == null) {
            return ResponseEntity.badRequest().body("Text description is required");
        }
        UUID cardRequestId = UUID.fromString(request.getCardRequestId()); // Conversion de String à UUID
        String generatedText = request.getGeneratedText();
        System.out.println("Controller : Card Request ID: " + cardRequestId + ", Generated Text: " + generatedText);
        cardOrchestratorService.updateTextAndTriggerImageRequest(cardRequestId, generatedText);
        return ResponseEntity.ok("Text description received and image request triggered");
    }

}
