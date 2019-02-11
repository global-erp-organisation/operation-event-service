package com.ia.operation.commands.creation;

import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.events.created.UserCreatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreationCmd {
    @TargetAggregateIdentifier
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    @JsonProperty("company_id")
    private String companyId;
    private String password;
    
    
    public static UserCreatedEvent.UserCreatedEventBuilder of(UserCreationCmd cmd){
        return UserCreatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
    }
    
    public static UserCreationCmdBuilder from(UserCreationCmd cmd){
        return UserCreationCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
    }

}
