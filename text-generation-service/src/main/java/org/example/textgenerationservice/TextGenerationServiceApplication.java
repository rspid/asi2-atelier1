package org.example.textgenerationservice;

import org.example.textgenerationservice.model.GenerationRequest;
import org.example.textgenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class TextGenerationServiceApplication implements CommandLineRunner {

    @Autowired
    private MessageQueueService messageQueueService;

    public static void main(String[] args) {
        SpringApplication.run(TextGenerationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("TextGenerationService started");
    }
}
