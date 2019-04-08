package com.ia.operation.commands.creation;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.ia.operation.aggregates.AccountCategoryAggregate;
import com.ia.operation.events.created.AccountCategoryCreatedEvent;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class AccountCategoryCreationCmd extends CommandValidator<AccountCategoryCreationCmd>{
    @TargetAggregateIdentifier
    protected String id;

    private String description;
    
    
    public static AccountCategoryCreatedEvent.AccountCategoryCreatedEventBuilder eventFrom (AccountCategoryCreationCmd cmd){
        /*@formatter:off*/
        return AccountCategoryCreatedEvent.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
        /*@formatter:on*/
    }
    
    public static AccountCategoryCreationCmdBuilder cmdFrom (AccountCategoryCreationCmd cmd){
        /*@formatter:off*/
        return AccountCategoryCreationCmd.builder()
            .id(cmd.getId())
            .description(cmd.getDescription());
        /*@formatter:on*/
    }
    
    @Override
    public ValidationResult<AccountCategoryCreationCmd> validate(AggregateHelper util) {
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
