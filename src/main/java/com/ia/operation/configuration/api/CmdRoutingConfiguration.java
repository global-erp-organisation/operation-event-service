package com.ia.operation.configuration.api;

import com.ia.operation.configuration.api.doc.*;
import com.ia.operation.configuration.axon.AxonProperties;
import com.ia.operation.configuration.api.doc.*;
import com.ia.operation.handlers.cmd.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class CmdRoutingConfiguration {

    private final AxonProperties properties;

    @Bean
    public RouterFunction<ServerResponse> companyCmdRoutes(CompanyCmdHandler handler, CompanyApiDoc doc) {
        return route().POST(String.format("%scompanies", properties.getRootApiPath()), handler::companyAdd, doc.companyAdd()).build()
                .and(route().DELETE(String.format("%scompanies/{%s}", properties.getRootApiPath(), COMPANY_ID_KEY), handler::companyRemove, doc.companyRemove()).build())
                .and(route().PUT(String.format("%scompanies/{%s}", properties.getRootApiPath(), COMPANY_ID_KEY), handler::companyUpdate, doc.companyUpdate()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> userCmdRoutes(UserCmdHandler handler, UserApiDoc doc) {
        return route().POST(String.format("%scompanies/{%s}/users", properties.getRootApiPath(), COMPANY_ID_KEY), handler::userAdd, doc.userAdd()).build()
                .and(route().DELETE(String.format("%susers/{%s}", properties.getRootApiPath(), USER_ID_KEY), handler::userDelete, doc.userDelete()).build())
                .and(route().PUT(String.format("%susers/{%s}", properties.getRootApiPath(), USER_ID_KEY), handler::userUpdate, doc.userUpdate()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> categoryCmdRoutes(AccountCategoryCmdHandler handler, AccountCategoryApiDoc doc) {
        return route().POST(String.format("%scategories", properties.getRootApiPath()), handler::categoryAdd, doc.categoryAdd()).build()
                .and(route().DELETE(String.format("%scategories/{%s}", properties.getRootApiPath(), CATEGORY_ID_KEY), handler::categoryDelete, doc.categoryDelete()).build())
                .and(route().PUT(String.format("%scategories/{%s}", properties.getRootApiPath(), CATEGORY_ID_KEY), handler::categoryUpdate, doc.categoryUpdate()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> accountCmdRoutes(AccountCmdHandler handler, AccountApiDoc doc) {
        return route().POST(String.format("%scategories/{%s}/users/{%s}/accounts", properties.getRootApiPath(), CATEGORY_ID_KEY, USER_ID_KEY), handler::accountAdd, doc.accountAdd()).build()
                .and(route().DELETE(String.format("%saccounts/{%s}", properties.getRootApiPath(), ACCOUNT_ID_KEY), handler::accountDelete, doc.accountDelete()).build())
                .and(route().PUT(String.format("%saccounts/{%s}", properties.getRootApiPath(), ACCOUNT_ID_KEY), handler::accountUpdate, doc.accountUpdate()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> periodCmdRoutes(PeriodCmdHandler handler, PeriodApiDoc doc) {
        return route().POST(String.format("%speriods", properties.getRootApiPath()), handler::periodAdd, doc.periodAdd()).build();
    }

    @Bean
    public RouterFunction<ServerResponse> projectionCmdRoutes(ProjectionCmdHandler handler, ProjectionApiDoc doc) {
        return route().POST(String.format("%saccounts/{%s}/periods/{%s}/projections", properties.getRootApiPath(), ACCOUNT_ID_KEY, PERIOD_ID_KEY), handler::projectionAdd, doc.projectionAdd()).build()
                .and(route().POST(String.format("%speriods/{%s}/projections", properties.getRootApiPath(), YEAR_KEY), handler::projectionGenerate, doc.projectionGenerate()).build())
                .and(route().DELETE(String.format("%sprojections/{%s}", properties.getRootApiPath(), PROJECTION_ID_KEY), handler::projectionDelete, doc.projectionDelete()).build())
                .and(route().PUT(String.format("%sprojections/{%s}", properties.getRootApiPath(), PROJECTION_ID_KEY), handler::projectionUpdate, doc.projectionUpdate()).build());
    }

    @Bean
    public RouterFunction<ServerResponse> operationCmdRoutes(OperationCmdHandler handler, OperationApiDoc doc) {
        return
                route().POST(String.format("%saccounts/{%s}/operations", properties.getRootApiPath(), ACCOUNT_ID_KEY), handler::operationAdd, doc.operationAdd()).build()
                        .and(route().DELETE(String.format("%soperations/{%s}", properties.getRootApiPath(), OPERATION_ID_KEY), handler::operationDelete, doc.operationDelete()).build())
                        .and(route().PUT(String.format("%soperations/{%s}", properties.getRootApiPath(), OPERATION_ID_KEY), handler::operationUpdate, doc.operationUpdate()).build())
                        .and(route().POST(String.format("%soperations", properties.getRootApiPath()), handler::operationTransfert, doc.operationTransfert()).build());
    }
}
