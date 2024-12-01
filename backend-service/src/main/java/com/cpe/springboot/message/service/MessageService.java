package com.cpe.springboot.message.service;

import com.cpe.springboot.message.model.MessageModel;
import com.cpe.springboot.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(MessageModel message) {
        messageRepository.save(message);
    }
}

