package com.ia.operation.configuration.api.doc;

import com.ia.operation.commands.creation.OperationCreationCmd;
import com.ia.operation.commands.creation.OperationTransfertCmd;
import com.ia.operation.commands.delete.OperationDeletionCmd;
import com.ia.operation.commands.update.OperationUpdateCmd;
import com.ia.operation.documents.Operation;
import com.ia.operation.handlers.cmd.OperationCmdHandler;
import com.ia.operation.handlers.query.OperationQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

@Component
public class OperationApiDoc {
    public Consumer<Builder> operationAdd() {
        return ops -> ops
                .beanClass(OperationCmdHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationAdd")
                .beanMethod("operationAdd")
                .summary("Initiate an operation creation command.")
                .description("Initiate an operation creation command.")
                .requestBody(requestBodyBuilder().implementation(OperationCreationCmd.class))
                .parameter(parameterBuilder().description("account identifier")
                        .name(ACCOUNT_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("201")
                        .description("Operation creation command accepted.")
                        .implementation(OperationCreationCmd.class));
    }

    public Consumer<Builder> operationTransfert() {
        return ops -> ops
                .beanClass(OperationCmdHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationTransfert")
                .beanMethod("operationTransfert")
                .summary("Initiate an operation transfer command.")
                .description("Initiate an operation transfer command.")
                .requestBody(requestBodyBuilder().implementation(OperationTransfertCmd.class))
                .response(responseBuilder().responseCode("201")
                        .description("Operation transfert command accepted.")
                        .implementation(OperationTransfertCmd.class));
    }

    public Consumer<Builder> operationDelete() {
        return ops -> ops
                .beanClass(OperationCmdHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationDelete")
                .beanMethod("operationDelete")
                .summary("Initiate an operation deletion command.")
                .description("Initiate an operation deletion command.")
                .parameter(parameterBuilder().description("operation identifier")
                        .name(OPERATION_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Operation deletion command accepted.")
                        .implementation(OperationDeletionCmd.class));
    }

    public Consumer<Builder> operationUpdate() {
        return ops -> ops
                .beanClass(OperationCmdHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationUpdate")
                .beanMethod("operationUpdate")
                .summary("Initiate an operation update command.")
                .description("Initiate an operation update command.")
                .requestBody(requestBodyBuilder().implementation(OperationUpdateCmd.class).required(true))
                .parameter(parameterBuilder().description("operation identifier")
                        .name(OPERATION_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("201")
                        .description("Operation update command accepted.")
                        .implementation(OperationCreationCmd.class));
    }

    public Consumer<Builder> operationGet() {
        return ops -> ops
                .beanClass(OperationQueryHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationGet")
                .beanMethod("operationGet")
                .summary("Retrieve and existing operation")
                .description("Retrieve and existing operation.")
                .parameter(parameterBuilder().description("operation identifier")
                        .name(OPERATION_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("Operation successfully retrieved.")
                        .implementation(Operation.class));
    }

    public Consumer<Builder> operationByYear() {
        return ops -> ops
                .beanClass(OperationQueryHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationsByYear")
                .beanMethod("operationsByYear")
                .summary("Retrieve user's operations for a given year")
                .description("Retrieve user's operations for a given year")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .parameter(parameterBuilder().description("target year")
                        .name(YEAR_KEY)
                        .in(ParameterIn.QUERY))
                .response(responseBuilder().responseCode("200")
                        .description("Operations successfully retrieved.")
                        .implementationArray(Operation.class));
    }

    public Consumer<Builder> operationsByMonth() {
        return ops -> ops
                .beanClass(OperationQueryHandler.class)
                .tags(new String[]{"Operations"})
                .operationId("operationsByMonth")
                .beanMethod("operationsByMonth")
                .summary("Retrieve user's operations for a given month")
                .description("Retrieve user's operations for a given month")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .parameter(parameterBuilder().description("target month")
                        .name(PERIOD_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("Operations successfully retrieved.")
                        .implementationArray(Operation.class));
    }
}
