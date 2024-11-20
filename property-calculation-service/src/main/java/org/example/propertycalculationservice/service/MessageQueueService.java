package org.example.propertycalculationservice.service;

import org.example.propertycalculationservice.model.ImagePropertiesRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessageToQueue(ImagePropertiesRequest request) {
        try {
            String messageJson = objectMapper.writeValueAsString(request);
            jmsTemplate.convertAndSend("imagePropertiesQueue", messageJson);
        } catch (Exception e) {
            // Gérer l'exception de manière appropriée
            e.printStackTrace();
        }
    }
}