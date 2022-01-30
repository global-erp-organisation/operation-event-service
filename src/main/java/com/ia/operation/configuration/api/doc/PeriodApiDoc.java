package com.ia.operation.configuration.api.doc;

import com.ia.operation.commands.creation.PeriodCreationCmd;
import com.ia.operation.documents.Period;
import com.ia.operation.handlers.Handler;
import com.ia.operation.handlers.cmd.PeriodCmdHandler;
import com.ia.operation.handlers.query.PeriodQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

@Component
public class PeriodApiDoc {

    public Consumer<Builder> periodAdd() {
        return ops -> ops
                .tags(new String[]{"Periods"})
                .beanClass(PeriodCmdHandler.class)
                .operationId("periodAdd")
                .beanMethod("periodAdd")
                .summary("Initiate the period generation for a given year")
                .description("Initiate the period generationfor a given year.")
                .parameter(parameterBuilder().description("target year")
                        .name(Handler.YEAR_KEY)
                        .implementation(Integer.class)
                        .in(ParameterIn.QUERY).required(true))
                .response(responseBuilder().responseCode("200")
                        .description("Periods generation command accepted.")
                        .implementationArray(PeriodCreationCmd.class));
    }

    public Consumer<Builder> periodGetByYear() {
        return ops -> ops
                .tags(new String[]{"Periods"})
                .beanClass(PeriodQueryHandler.class)
                .operationId("periodGetByYear")
                .beanMethod("periodGetByYear")
                .summary("Retrieve period by year")
                .description("Retrieve and existing periods by year.")
                .parameter(parameterBuilder().description("year")
                        .name(Handler.YEAR_KEY)
                        .implementation(Integer.class)
                        .in(ParameterIn.QUERY))
                .response(responseBuilder().responseCode("200")
                        .description("Period successfully retrieved.")
                        .implementationArray(Period.class));
    }

    public Consumer<Builder> periodGetById() {
        return ops -> ops
                .tags(new String[]{"Periods"})
                .beanClass(PeriodQueryHandler.class)
                .operationId("periodGetById")
                .beanMethod("periodGetById")
                .summary("Retrieve period by Id")
                .description("Retrieve and existing period by Id.")
                .parameter(parameterBuilder().description("period-id")
                        .name(Handler.PERIOD_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("Period successfully retrieved.")
                        .implementation(Period.class));
    }
}
