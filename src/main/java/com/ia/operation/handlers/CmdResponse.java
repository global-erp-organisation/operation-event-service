package com.ia.operation.handlers;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonInclude(value = Include.NON_EMPTY)
public class CmdResponse<T,E> {
    private T body;
    private Error<E> error; 

    @Value
    @Builder
    public static class Error<E> {
        private HttpStatus status;
        private E body;
    }
}
