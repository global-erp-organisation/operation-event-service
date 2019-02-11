package com.ia.operation.commands.update;

import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.events.updated.UserUpdatedEvent;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserUpdateCmd {
    @TargetAggregateIdentifier
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    @JsonProperty("company_id")
    private String companyId;
    private String password;
    
    public static UserUpdatedEvent.UserUpdatedEventBuilder of(UserUpdateCmd cmd){
        return UserUpdatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
    }
    
    public static UserUpdateCmdBuilder from(UserUpdateCmd cmd){
        return UserUpdateCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
    }

}
