package com.ia.operation.commands.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ia.operation.aggregates.CompanyAggregate;
import com.ia.operation.aggregates.UserAggregate;
import com.ia.operation.events.updated.UserUpdatedEvent;
import com.ia.operation.helper.AggregateHelper;
import com.ia.operation.helper.validator.CommandValidator;

import io.netty.util.internal.StringUtil;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserUpdateCmd extends CommandValidator<UserUpdateCmd> {
    @TargetAggregateIdentifier
    protected String id;

    private String email;
    private String description;
    private Map<String, Object> details;
    @JsonProperty("company_id")
    private String companyId;
    private String password;

    public static UserUpdatedEvent.UserUpdatedEventBuilder eventFrom(UserUpdateCmd cmd) {
        /*@formatter:off*/
        return UserUpdatedEvent.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
        /*@formatter:on*/
    }

    public static UserUpdateCmdBuilder cmdFrom(UserUpdateCmd cmd) {
        /*@formatter:off*/
        return UserUpdateCmd.builder()
                .id(cmd.getId())
                .description(cmd.getDescription())
                .details(cmd.getDetails())
                .companyId(cmd.getCompanyId())
                .email(cmd.getEmail())
                .password(cmd.getPassword());
        /*@formatter:on*/
    }

    @Override
    public ValidationResult<UserUpdateCmd> validate(AggregateHelper util) {
        final List<String> errors = new ArrayList<>();
        if (StringUtils.isEmpty(id)) {
            errors.add("User identifier shouldn't be null or empty");
        } else {
            final Optional<UserAggregate> u = util.aggregateGet(id, UserAggregate.class);
            if (u.isPresent()) {
                setCompanyId(companyId == null ? u.get().getCompanyId() : companyId);
            } else {
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
        } else {
            if (!util.aggregateGet(companyId, CompanyAggregate.class).isPresent()) {
                errors.add("The Company with id " + id + " doesnt exist");
            }
        }
        return buildResult(errors);
    }
}
