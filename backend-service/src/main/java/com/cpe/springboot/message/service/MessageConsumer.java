package com.cpe.springboot.message.service;

import com.cpe.springboot.message.model.MessageDTO;
import com.cpe.springboot.message.model.MessageModel;
import com.cpe.springboot.message.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageConsumer {

    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageConsumer(MessageService messageService, ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "messageQueue") // Nom de la queue
    public void consumeMessage(String messageJson) {
        try {
            // Convertir le JSON reçu en objet MessageDTO
            MessageDTO messageDTO = objectMapper.readValue(messageJson, MessageDTO.class);

            // Créer un MessageModel à partir du DTO
            MessageModel message = new MessageModel(
                    messageDTO.getSenderId(),
                    messageDTO.getReceiverId(),
                    messageDTO.getContent(),
                    messageDTO.getMessageDate()
            );

            // Enregistrer en base
            messageService.saveMessage(message);
            System.out.println("Message saved with ID: " + message.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
