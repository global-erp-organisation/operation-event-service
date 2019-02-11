package com.ia.operation.configuration;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.kaiso.relmongo.config.EnableRelMongo;

@Configuration
@EnableRelMongo
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
