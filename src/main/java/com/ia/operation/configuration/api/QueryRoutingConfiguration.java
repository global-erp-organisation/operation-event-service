package com.ia.operation.configuration.api;

import com.ia.operation.configuration.axon.AxonProperties;
import com.ia.operation.configuration.doc.*;
import com.ia.operation.handlers.query.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class QueryRoutingConfiguration {

    private final AxonProperties properties;

    @Bean
    public RouterFunction<ServerResponse> companyQueryRoutes(CompanyQueryHandler handler, CompanyApiDoc doc) {
        return route().GET(String.format("%scompanies/{%s}", properties.getRootApiPath(), COMPANY_ID_KEY), handler::companyGet, doc.companyGet()).build()
                .and(route().GET(String.format("%scompanies", properties.getRootApiPath()), handler::companyGetAll, doc.companyGetAll()).build());
    }

   @Bean
    public RouterFunction<ServerResponse> categoryQueryRoutes(AccountCategoryQueryHandler handler, AccountCategoryApiDoc doc) {
        return route().GET(String.format("%scategories/{%s}", properties.getRootApiPath(), CATEGORY_ID_KEY), handler::accountCategoryGet, doc.accountCategoryGet()).build()
                .and(route().GET(String.format("%scategories", properties.getRootApiPath()), handler::accountCategoryGetAll, doc.accountCategoryGetAll()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> accountQueryRoutes(AccountQueryHandler handler, AccountApiDoc doc) {
        return route().GET(String.format("%saccounts/{%s}", properties.getRootApiPath(), ACCOUNT_ID_KEY), handler::accountGet, doc.accountGet()).build()
                .and(route().GET(String.format("%susers/{%s}/accounts", properties.getRootApiPath(), USER_ID_KEY), handler::accountGetAll, doc.accountGetAll()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> projectionQueryRoute(ProjectionQueryHandler handler, ProjectionApiDoc doc) {
        return route().GET(String.format("%speriods/{%s}/users/{%s}/projections", properties.getRootApiPath(), YEAR_KEY, USER_ID_KEY), handler::projectionByYear, doc.projectionByYear()).build()
                .and(route().GET(String.format("%speriods/{%s}/accounts/{%s}/projections", properties.getRootApiPath(), YEAR_KEY, ACCOUNT_ID_KEY), handler::projectionByAccount, doc.projectionByAccount()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> operationQueryRoute(OperationQueryHandler handler, OperationApiDoc doc) {
        return route().GET(String.format("%susers/{%s}/operations", properties.getRootApiPath(), USER_ID_KEY), handler::operationsByYear, doc.operationByYear()).build()
                .and(route().GET(String.format("%soperations/{%s}", properties.getRootApiPath(), OPERATION_ID_KEY), handler::operationGet, doc.operationGet()).build())
                .and(route().GET(String.format("%susers/{%s}/periods/{%s}/operations", properties.getRootApiPath(), USER_ID_KEY, PERIOD_ID_KEY), handler::operationsByMonth, doc.operationsByMonth()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> periodQueryRoutes(PeriodQueryHandler handler, PeriodApiDoc doc) {
        return route().GET(String.format("%speriods", properties.getRootApiPath()), handler::periodGetByYear, doc.periodGetByYear()).build()
                .and(route().GET(String.format("%speriods/{%s}", properties.getRootApiPath(), PERIOD_ID_KEY), handler::periodGetById, doc.periodGetById()).build());
    }



    public RouterFunction<ServerResponse> userQueryRoutes(UserQueryHandler handler, UserApiDoc doc) {
        return route().GET(String.format("%susers/{%s}",properties.getRootApiPath(),USER_ID_KEY), handler::userGet,doc.userGet()).build()
                .and(route().GET(String.format("%susers",properties.getRootApiPath()), handler::userGetByEmail,doc.userGetByEmail()).build());
    }
    
    @Bean
    public RouterFunction<ServerResponse> dashboardQueryRoutes(DashboardQueryHandler handler, DashboardApiDoc doc) {
        return route().GET("/users/{userId}/dashboard", handler::dashboardGet,doc.dashboardGet()).build();
    }
}
