package com.ia.operation.documents;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ia.operation.events.created.UserCreatedEvent;
import com.ia.operation.events.updated.UserUpdatedEvent;

import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@JsonInclude(value=Include.NON_EMPTY)
public class User {
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;
    private String password;
    
    public static User of(UserCreatedEvent event, Company company) {
        /*@formatter:off*/
        return User.builder()
                .id(event.getId())
                .description(event.getDescription())
                .details(event.getDetails())
                .company(company)
                .email(event.getEmail())
                .password(event.getPassword())
                .build();
        /*@formatter:on*/
    }
    
    public static User of(UserUpdatedEvent event, Company company) {
        /*@formatter:off*/
        return User.builder()
                .id(event.getId())
                .description(event.getDescription())
                .details(event.getDetails())
                .company(company)
                .email(event.getEmail())
                .password(event.getPassword())
                .build();
        /*@formatter:on*/
    }

}
