package com.ia.operation.commands.creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.aggregates.UserAggregate;
import com.ia.operation.events.created.UserCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserCreationCmd  extends CommandValidator<UserCreationCmd>{
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
    
    
    @Override
    public ValidationResult<UserCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("User identifier shouldn't be null or empty");
        }else {
            if (util.aggregateGet(id, UserAggregate.class).isPresent()) {
                errors.add("The User with id " + id + " already exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("User description shouldn't be null or empty");
        }
        
        if (StringUtil.isNullOrEmpty(email)) {
            errors.add("User email shouldn't be null or empty");
        }

        if (StringUtils.isEmpty(companyId)) {
            errors.add("User identifier shouldn't be null or empty");
        }else {
            if (!util.aggregateGet(companyId, CompanyAggregate.class).isPresent()) {
                errors.add("The Company with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
