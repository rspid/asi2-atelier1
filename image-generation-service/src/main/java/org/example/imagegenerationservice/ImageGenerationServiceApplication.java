package org.example.imagegenerationservice;

import org.example.imagegenerationservice.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ImageGenerationServiceApplication implements CommandLineRunner {

    @Autowired
    private MessageQueueService messageQueueService;

    public static void main(String[] args) {
        SpringApplication.run(ImageGenerationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Code to execute when the application starts, for example:
        System.out.println("Image Generation Service Application has started.");

        // You can use the messageQueueService here if needed
        // messageQueueService.processMessages(); // Example usage
    }
}
