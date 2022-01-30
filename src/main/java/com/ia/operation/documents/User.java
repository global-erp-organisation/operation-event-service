package com.ia.operation.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ia.operation.events.created.UserCreatedEvent;
import com.ia.operation.events.updated.UserUpdatedEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Builder
@Data
@JsonInclude(value=Include.NON_EMPTY)
public class User {
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    //@ManyToOne(fetch = FetchType.EAGER)
    private Company company;
    @JsonIgnore
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
