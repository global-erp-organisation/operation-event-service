package com.ia.operation.configuration.routing;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ia.operation.handlers.query.CompanyQueryHandler;
import com.ia.operation.handlers.query.DashboardQueryHandler;
import com.ia.operation.handlers.query.AccountCategoryQueryHandler;
import com.ia.operation.handlers.query.AccountQueryHandler;
import com.ia.operation.handlers.query.PeriodQueryHandler;
import com.ia.operation.handlers.query.ProjectionQueryHandler;
import com.ia.operation.handlers.query.UserQueryHandler;
import com.ia.operation.handlers.query.OperationQueryHandler;

@Configuration
public class QueryRoutingConfiguration {
    @Bean
    public RouterFunction<ServerResponse> companyQueryRoutes(CompanyQueryHandler handler) {
        return RouterFunctions.route(GET("/companies/{companyId}"), handler::companyGet)
                .andRoute(GET("/companies"), handler::companyGetAll);
    }

    @Bean
    public RouterFunction<ServerResponse> categoryQueryRoutes(AccountCategoryQueryHandler handler) {
        return RouterFunctions.route(GET("/categories/{categoryId}"), handler::accountCategoryGet)
                .andRoute(GET("/categories"), handler::accountCategoryGetAll);
    }

    @Bean
    public RouterFunction<ServerResponse> accountQueryRoutes(AccountQueryHandler handler) {
        return RouterFunctions.route(GET("/accounts/{accountId}"), handler::accountGet)
                .andRoute(GET("/users/{userId}/accounts"), handler::accountGetAll);
    }

    @Bean
    public RouterFunction<ServerResponse> projectionQueryRoute(ProjectionQueryHandler handler) {
        return RouterFunctions.route(GET("/periods/{year}/users/{userId}/projections"), handler::projectionByYear)
                .andRoute(GET("/periods/{year}/accounts/{accountId}/projections"), handler::projectionByAccount);
    }

    @Bean
    public RouterFunction<ServerResponse> operationQueryRoute(OperationQueryHandler handler) {
        return RouterFunctions.route(GET("/periods/{year}/users/{userId}/operations"), handler::operationGetByear)
                .andRoute(GET("/operations/{operationId}"), handler::operationGet)
                .andRoute(GET("/users/{userId}/operations"), handler::operationGetAll);
    }

    @Bean
    public RouterFunction<ServerResponse> periodQueryRoutes(PeriodQueryHandler handler) {
        return RouterFunctions.route(GET("/periods/{year}"), handler::periodGetByYear);
    }

    @Bean
    public RouterFunction<ServerResponse> userQueryRoutes(UserQueryHandler handler) {
        return RouterFunctions.route(GET("/users/{userId}"), handler::userGet);
    }
    
    @Bean
    public RouterFunction<ServerResponse> dashboardQueryRoutes(DashboardQueryHandler handler) {
        return RouterFunctions.route(GET("/users/{userId}/dashboard"), handler::dashboardGet);
    }
}
