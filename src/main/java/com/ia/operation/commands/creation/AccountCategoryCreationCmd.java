package com.ia.operation.commands.creation;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.AccountCategoryAggregate;
import com.ia.operation.events.created.AccountCategoryCreatedEvent;
import com.ia.operation.util.AggregateUtil;
import com.ia.operation.util.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountCategoryCreationCmd extends CommandValidator<AccountCategoryCreationCmd>{
    @TargetAggregateIdentifier
    private String id;
    private String description;
    
    public static AccountCategoryCreatedEvent.AccountCategoryCreatedEventBuilder from (AccountCategoryCreationCmd cmd){
        return AccountCategoryCreatedEvent.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
    }
    
    public static AccountCategoryCreationCmdBuilder of (AccountCategoryCreationCmd cmd){
        return AccountCategoryCreationCmd.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
    }
    
    @Override
    public ValidationResult<AccountCategoryCreationCmd> validate(AggregateUtil util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("Category identifier shouldn't be null or empty");
        }else {
            if (util.aggregateGet(id, AccountCategoryAggregate.class).isPresent()) {
                errors.add("The category with id " + id + " already exist");
            }
        }
        if (StringUtil.isNullOrEmpty(description)) {
            errors.add("Category description shouldn't be null or empty");
        }
        return buildResult(errors);
    }
}
