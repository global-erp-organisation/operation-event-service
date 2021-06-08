package com.ia.operation.configuration.api.doc;

import com.ia.operation.commands.creation.AccountCreationCmd;
import com.ia.operation.commands.delete.AccountDeletionCmd;
import com.ia.operation.commands.update.AccountUpdateCmd;
import com.ia.operation.documents.Account;
import com.ia.operation.handlers.cmd.AccountCmdHandler;
import com.ia.operation.handlers.query.AccountQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

@Component
public class AccountApiDoc {

    public Consumer<Builder> accountAdd() {
        return ops -> ops
                .beanClass(AccountCmdHandler.class)
                .tags(new String[]{"Accounts"})
                .operationId("accountAdd")
                .beanMethod("accountAdd")
                .summary("Initiate the account creation command")
                .description("Initiate the account  creation command")
                .parameter(parameterBuilder().description("category identifier")
                        .name(CATEGORY_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .requestBody(requestBodyBuilder().required(true)
                        .implementation(AccountCreationCmd.class))
                .response(responseBuilder().responseCode("200")
                        .description("Account creation command accepted.")
                        .implementation(AccountCreationCmd.class));
    }

    public Consumer<Builder> accountDelete() {
        return ops -> ops
                .beanClass(AccountCmdHandler.class)
                .tags(new String[]{"Accounts"})
                .operationId("accountDelete")
                .beanMethod("accountDelete")
                .summary("Initiate the account category deletion command")
                .description("Initiate the account category deletion command")
                .parameter(parameterBuilder().description("account identifier")
                        .name(ACCOUNT_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Account creation command accepted.")
                        .implementation(AccountDeletionCmd.class));
    }

    public Consumer<Builder> accountUpdate() {
        return ops -> ops
                .beanClass(AccountCmdHandler.class)
                .tags(new String[]{"Accounts"})
                .operationId("accountUpdate")
                .beanMethod("accountUpdate")
                .summary("Initiate the account  update command")
                .description("Initiate the account category update command")
                .requestBody(requestBodyBuilder().required(true)
                        .implementation(AccountUpdateCmd.class))
                .parameter(parameterBuilder().description("category identifier")
                        .name(ACCOUNT_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Account update command accepted.")
                        .implementation(AccountUpdateCmd.class));
    }

    public Consumer<Builder> accountGet() {
        return ops -> ops
                .beanClass(AccountQueryHandler.class)
                .tags(new String[]{"Accounts"})
                .operationId("accountGet")
                .beanMethod("accountGet")
                .summary("Retrieve an existing account")
                .description("Retrieve an existing account")
                .parameter(parameterBuilder().description("account identifier")
                        .name(ACCOUNT_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200")
                        .description("Account successfully retrieved.")
                        .implementation(Account.class));
    }

    public Consumer<Builder> accountGetAll() {
        return ops -> ops
                .beanClass(AccountQueryHandler.class)
                .tags(new String[]{"Accounts"})
                .operationId("accountGetAll")
                .beanMethod("accountGetAll")
                .summary("Retrieve an existing accounts for a given user")
                .description("Retrieve an existing accounts for a given user")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200")
                        .description("User accounts successfully retrieved.")
                        .implementationArray(Account.class));
    }
}
