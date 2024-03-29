package com.ia.operation.helper.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ia.operation.commands.ICommand;
import com.ia.operation.helper.AggregateHelper;

import lombok.Builder;
import lombok.Data;

/**
 * Validation interface.
 * 
 * @author martinblaise
 * @param <V> Type of the object that need to be validated.
 */

public  abstract class CommandValidator<V extends ICommand> implements ICommand {

    public ValidationResult<V> validate() {
        return buildResult(new ArrayList<>());
    }

    public ValidationResult<V> validate(AggregateHelper util) {
        return validate();
    }
    

    protected ValidationResult<V> buildResult(List<String> errors) {
        return ValidationResult.<V>builder().errors(errors).validated(Optional.of(this)).isValid(errors.isEmpty()).build();
    }

    @Data
    @Builder
    public static class ValidationResult<V extends ICommand> {
        private boolean isValid;
        private List<String> errors;
        private Optional<CommandValidator<V>> validated;
    }
    
}
