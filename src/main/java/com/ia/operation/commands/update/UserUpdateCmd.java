package com.ia.operation.commands.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.aggregates.UserAggregate;
import com.ia.operation.events.updated.UserUpdatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserUpdateCmd extends CommandValidator<UserUpdateCmd>{
    @TargetAggregateIdentifier
    private String id;
    private String email;
    private String description;
    private Map<String, Object> details;
    @JsonProperty("company_id")
    private String companyId;
    private String password;
    
    public static UserUpdatedEvent.UserUpdatedEventBuilder eventFrom(UserUpdateCmd cmd){
        return UserUpdatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
    }
    
    public static UserUpdateCmdBuilder cmdFrom(UserUpdateCmd cmd){
        return UserUpdateCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
    }
    
    
    @Override
    public ValidationResult<UserUpdateCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("User identifier shouldn't be null or empty");
        }else {
            if (!util.aggregateGet(id, UserAggregate.class).isPresent()) {
                errors.add("The User with id " + id + " doesnt exist");
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
