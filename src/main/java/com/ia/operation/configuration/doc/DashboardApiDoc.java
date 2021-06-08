package com.ia.operation.configuration.doc;

import com.ia.operation.documents.User;
import com.ia.operation.documents.views.DashboardView;
import com.ia.operation.handlers.query.DashboardQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.Consumer;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

@Component
public class DashboardApiDoc {

    public Consumer<Builder> dashboardGet() {
        return ops -> ops
                .tags(new String[]{"dashboard"})
                .beanClass(DashboardQueryHandler.class)
                .operationId("dashboardGet")
                .beanMethod("dashboardGet")
                .summary("Retrieve a user dashboard")
                .description("Retrieve a user dashboard")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .implementation(String.class)
                        .in(ParameterIn.PATH))
                .parameter(parameterBuilder().description("Start date")
                        .name(START_DATE_KEY)
                        .implementation(LocalDate.class)
                        .in(ParameterIn.QUERY).required(true))
                .parameter(parameterBuilder().description("End date")
                        .name(END_DATE_KEY)
                        .implementation(LocalDate.class)
                        .in(ParameterIn.QUERY).required(true))
                .parameter(parameterBuilder().description("Extract daily datas")
                        .name(DAILY_KEY)
                        .implementation(Boolean.class)
                        .in(ParameterIn.QUERY).required(false))
                .parameter(parameterBuilder().description("Extract monthly datas")
                        .name(MONTHLY_KEY)
                        .implementation(Boolean.class)
                        .in(ParameterIn.QUERY).required(false))
                .parameter(parameterBuilder().description("Extract yearly datas")
                        .name(YEAR_KEY)
                        .implementation(Boolean.class)
                        .in(ParameterIn.QUERY).required(false))
                .response(responseBuilder().responseCode("200")
                        .description("User dashboard successfully retrieved.")
                        .implementationArray(DashboardView.class));
    }

}
