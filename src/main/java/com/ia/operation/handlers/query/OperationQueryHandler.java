package com.ia.operation.handlers.query;

import java.util.concurrent.ExecutionException;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ia.operation.documents.Projection;
import com.ia.operation.queries.operation.OperationGetByIdQuery;
import com.ia.operation.queries.operation.OperationGetByUser;
import com.ia.operation.queries.projection.ProjectionByOperationQuery;
import com.ia.operation.queries.projection.ProjectionByPeriodQuery;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
public class OperationQueryHandler {
    private final QueryGateway gateway;

    @GetMapping(value="users/{userId}/operations")
    public Object operationGetAll(@PathVariable String userId) throws InterruptedException, ExecutionException {
        return gateway.query(OperationGetByUser.builder().build(), Object.class).get();
    }

    @GetMapping(value = "operations/{operationId}")
    public Object operationGet(@PathVariable String operationId) throws InterruptedException, ExecutionException {
        return gateway.query(OperationGetByIdQuery.builder().operationId(operationId).build(), Object.class).get();
    }

    @GetMapping(value = "/{operationId}/users/{userId}/projections")
    public Object projectionByOperation(@PathVariable String operationId,@PathVariable String userId) throws InterruptedException, ExecutionException {
        return gateway.query(ProjectionByOperationQuery.builder().operationId(operationId).userId(userId).build(), Object.class).get();
    }
   
    @SuppressWarnings("unchecked")
    @GetMapping(value = "periods/{periodId}/users/{}userId/projections")
    public Flux<Projection>  projectionsGenerationQuery(@PathVariable String periodId, @PathVariable String userId) throws InterruptedException, ExecutionException {
        return (Flux<Projection>) gateway.query(ProjectionByPeriodQuery.builder().periodId(periodId).userId(userId).build(), Object.class).get();
     }
}
