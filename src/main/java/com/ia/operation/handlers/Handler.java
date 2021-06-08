package com.ia.operation.handlers;

import org.axonframework.common.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Handler {
    String ACCOUNT_ID_KEY = "accountId";
    String CATEGORY_ID_KEY = "categoryId";
    String COMPANY_ID_KEY = "companyId";
    String MISSING_REQUEST_BODY_KEY = "The request body is missing.";
    String OPERATION_ID_KEY = "operationId";
    String PERIOD_ID_KEY = "periodId";
    String PROJECTION_ID_KEY = "projectionId";
    String USER_ID_KEY = "userId";
    String YEAR_KEY = "year";
    String END_DATE_KEY = "end-date";
    String START_DATE_KEY = "start-date";
    String MISSING_QUERY_PARAM_PREFIX = "missing query param: ";
    String MISSING_PATH_VARIABLE_PREFIX = "missing path variable: ";
    String YEARLY_KEY = "yearly";
    String MONTHLY_KEY = "monthly";
    String DAILY_KEY = "daily";
    String EMAIL_KEY = "email";

    List<String> errors = new ArrayList<>();
    default void addError(String item) {
        errors.add(item);
    }
    default void errorReset() {
        errors.clear();
    }

    default   <T, E> Mono<ServerResponse> errorWithStatus(E errorDescription, HttpStatus status) {
        final CmdResponse.Error<E> error = CmdResponse.Error.<E>builder().status(status).body(errorDescription).build();
        return ServerResponse.status(status).body(Mono.just(CmdResponse.<T, E>builder().error(error).build()), CmdResponse.class);
    }

    default   <E> Mono<ServerResponse> internalErrorResponse(Supplier<E> message) {
        return errorWithStatus(message.get(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    default   <E> Mono<ServerResponse> badRequestError(E errorDescription) {
        return errorWithStatus(errorDescription, HttpStatus.BAD_REQUEST);
    }

    default void beanValidate(Object... beans) {
        for (Object o : beans) {
            Assert.notNull(o, () -> "null is not accepted for " + o.getClass().getSimpleName() + " object");
        }
    }

    default   <T> Mono<ServerResponse> badRequestComplete(Supplier<T> message, Class<T> type) {
        beanValidate(message);
        return ServerResponse.badRequest().body(Mono.just(message.get()), type);
    }
}
