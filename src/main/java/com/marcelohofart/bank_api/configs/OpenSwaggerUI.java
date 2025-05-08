package com.marcelohofart.bank_api.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;

@Component
public class OpenSwaggerUI {
    @Autowired
    private Environment environment;
    @EventListener(ApplicationReadyEvent.class)
    public void onReadyOpenSwaggerUI() {
        String port = environment.getProperty("server.port", "8080");
        String url = "http://localhost:" + port + "/swagger-ui.html";

        System.out.println("Swagger -> " + url);
    }
}
