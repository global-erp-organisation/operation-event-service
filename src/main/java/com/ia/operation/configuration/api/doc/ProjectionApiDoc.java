package com.ia.operation.configuration.api.doc;

import com.ia.operation.commands.creation.ProjectionCreationCmd;
import com.ia.operation.commands.delete.ProjectionDeletionCmd;
import com.ia.operation.commands.update.ProjectionUpdateCmd;
import com.ia.operation.documents.Projection;
import com.ia.operation.handlers.cmd.ProjectionCmdHandler;
import com.ia.operation.handlers.query.ProjectionQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

@Component
public class ProjectionApiDoc {
    public Consumer<Builder> projectionAdd() {
        return ops -> ops
                .tags(new String[]{"Projections"})
                .beanClass(ProjectionCmdHandler.class)
                .operationId("projectionAdd")
                .beanMethod("projectionAdd")
                .summary("Initiate the projection creation command.")
                .description("Initiate the projection creation command.")
                .requestBody(requestBodyBuilder().implementation(ProjectionCreationCmd.class).required(true))
                .parameter(parameterBuilder().description("account identifier")
                        .name(ACCOUNT_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .parameter(parameterBuilder().description("period identifier")
                        .name(PERIOD_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Projection creation command accepted.")
                        .implementationArray(ProjectionCreationCmd.class));
    }

    public Consumer<Builder> projectionUpdate() {
        return ops -> ops
                .tags(new String[]{"Projections"})
                .beanClass(ProjectionCmdHandler.class)
                .operationId("projectionUpdate")
                .beanMethod("projectionUpdate")
                .summary("Initiate the projection update command.")
                .description("Initiate the projection update command.")
                .requestBody(requestBodyBuilder().implementation(ProjectionCreationCmd.class).required(true))
                .parameter(parameterBuilder().description("projection identifier")
                        .name(PROJECTION_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Projection update command accepted.")
                        .implementationArray(ProjectionUpdateCmd.class));
    }

    public Consumer<Builder> projectionGenerate() {
        return ops -> ops
                .tags(new String[]{"Projections"})
                .beanClass(ProjectionCmdHandler.class)
                .operationId("projectionGenerate")
                .beanMethod("projectionGenerate")
                .summary("Initiate the projection generation command.")
                .description("Initiate the projection generation command.")
                .parameter(parameterBuilder().description("target year")
                        .name(YEAR_KEY)
                        .implementation(Integer.class)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Projection generation command accepted.")
                        .implementation(String.class));
    }

    public Consumer<Builder> projectionDelete() {
        return ops -> ops
                .tags(new String[]{"Projections"})
                .beanClass(ProjectionCmdHandler.class)
                .operationId("projectionDelete")
                .beanMethod("projectionDelete")
                .summary("Initiate the projection deletion command.")
                .description("Initiate the projection deletion command.")
                .parameter(parameterBuilder().description("projection identifier")
                        .name(PROJECTION_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Projection deletion command accepted.")
                        .implementation(ProjectionDeletionCmd.class));
    }

    public Consumer<Builder> projectionByYear() {

        return ops -> ops
                .tags(new String[]{"Projections"})
                .beanClass(ProjectionQueryHandler.class)
                .operationId("projectionByYear")
                .beanMethod("projectionByYear")
                .summary("Retrieve user's projections for a given year")
                .description("Retrieve user's projections for a given year")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .parameter(parameterBuilder().description("target year")
                        .name(YEAR_KEY).implementation(Integer.class)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("Projections successfully retrieved.")
                        .implementationArray(Projection.class));
    }

    public Consumer<Builder> projectionByAccount() {
        return ops -> ops
                .tags(new String[]{"Projections"})
                .beanClass(ProjectionQueryHandler.class)
                .operationId("projectionByAccount")
                .beanMethod("projectionByAccount")
                .summary("Retrieve user's projections for a given account")
                .description("Retrieve user's projections for a given account")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .parameter(parameterBuilder().description("target account identifier")
                        .name(ACCOUNT_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("Projections successfully retrieved.")
                        .implementationArray(Projection.class));
    }
}
