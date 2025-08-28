package com.example.bajajfinserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.bajajfinserv.service.WebhookService;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        webhookService.executeFlow();
    }
}
