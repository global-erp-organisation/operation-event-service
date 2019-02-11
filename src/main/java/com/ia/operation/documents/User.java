package com.ia.operation.documents;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.ia.operation.events.created.UserCreatedEvent;
import com.ia.operation.events.updated.UserUpdatedEvent;

import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
public class User {
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    private Company company;
    private String password;
    
    public static User of(UserCreatedEvent event, Company company) {
        return User.builder()
                .id(event.getId())
                .description(event.getDescription())
                .details(event.getDetails())
                .company(company)
                .email(event.getEmail())
                .password(event.getPassword())
                .build();
    }
    
    public static User of(UserUpdatedEvent event, Company company) {
        return User.builder()
                .id(event.getId())
                .description(event.getDescription())
                .details(event.getDetails())
                .company(company)
                .email(event.getEmail())
                .password(event.getPassword())
                .build();
    }

}
