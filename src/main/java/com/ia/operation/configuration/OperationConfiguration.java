package com.ia.operation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Locale;

@Configuration
public class OperationConfiguration {

    @Bean
    public Locale locale() {
        return Locale.US;
    }
    
    
    @Bean
    public WebClient webClient() {
        return WebClient.create("http://localhost:8093");
    }

}
