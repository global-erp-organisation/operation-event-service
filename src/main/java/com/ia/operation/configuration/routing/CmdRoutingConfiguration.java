package com.ia.operation.configuration.routing;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.handlers.cmd.CompanyCmdHandler;
import com.ia.operation.handlers.cmd.AccountCategoryCmdHandler;
import com.ia.operation.handlers.cmd.AccountCmdHandler;
import com.ia.operation.handlers.cmd.PeriodCmdHandler;
import com.ia.operation.handlers.cmd.ProjectionCmdHandler;
import com.ia.operation.handlers.cmd.OperationCmdHandler;
import com.ia.operation.handlers.cmd.UserCmdHandler;

@Configuration
public class CmdRoutingConfiguration {

    @Bean
    public RouterFunction<ServerResponse> companyCmdRoutes(CompanyCmdHandler handler) {
        return RouterFunctions
                .route(POST("/companies"), handler::companyAdd)
                .andRoute(DELETE("/companies/{companyId}"), handler::companyRemove)
                .andRoute(PUT("/companies/{companyId}"), handler::companyUpdate);
    }

    @Bean
    public RouterFunction<ServerResponse> userCmdRoutes(UserCmdHandler handler) {
        return RouterFunctions
                .route(POST("/companies/{companyId}/users"), handler::userAdd)
                .andRoute(DELETE("/users/{userId}"), handler::userDelete)
                .andRoute(PUT("/users/{userId}"), handler::userUpdate);
    }

    @Bean
    public RouterFunction<ServerResponse> categoryCmdRoutes(AccountCategoryCmdHandler handler) {
        return RouterFunctions
                .route(POST("/categories"), handler::categoryAdd)
                .andRoute(DELETE("/categories/{categoryId}"), handler::categoryDelete)
                .andRoute(PUT("/categories/{categoryId}"), handler::categoryUpdate);
    }

    @Bean
    public RouterFunction<ServerResponse> accountCmdRoutes(AccountCmdHandler handler) {
        return RouterFunctions
                .route(POST("/categories/{categoryId}/users/{userId}/accounts"), handler::accountAdd)
                .andRoute(DELETE("/accounts/{accountId}"), handler::accountDelete)
                .andRoute(PUT("/accounts/{accountId}"), handler::accountUpdate);
    }

    @Bean
    public RouterFunction<ServerResponse> periodCmdRoutes(PeriodCmdHandler handler) {
        return RouterFunctions.route(POST("/periods"), handler::periodAdd);
    }

    @Bean
    public RouterFunction<ServerResponse> projectionCmdRoutes(ProjectionCmdHandler handler) {
        return RouterFunctions
                .route(POST("/accounts/{accountId}/periods/{periodId}/projections"), handler::projectionAdd)
                .andRoute(POST("/periods/{year}/projections"), handler::projectionGenerate)
                .andRoute(DELETE("/projections/{projectionId}"), handler::projectionDelete)
                .andRoute(PUT("/projections/{projectionId}"), handler::projectionUpdate);
    }

    @Bean
    public RouterFunction<ServerResponse> operationCmdRoutes(OperationCmdHandler handler) {
        return RouterFunctions
                .route(POST("/accounts/{accountId}/operations"), handler::operationAdd)
                .andRoute(DELETE("/operations/{operationId}"), handler::operationDelete)
               .andRoute(PUT("/operations/{operationId}"), handler::operationUpdate);
    }
}
