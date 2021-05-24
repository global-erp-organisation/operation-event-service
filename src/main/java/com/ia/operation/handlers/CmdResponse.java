package com.ia.operation.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Builder
@Value
@JsonInclude(value = Include.NON_EMPTY)
public class CmdResponse<T,E> {
     T body;
     Error<E> error;

    @Value
    @Builder
    public static class Error<E> {
         HttpStatus status;
         E body;
    }
}
