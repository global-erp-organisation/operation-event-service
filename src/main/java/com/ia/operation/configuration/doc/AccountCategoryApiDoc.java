package com.ia.operation.configuration.doc;

import com.ia.operation.commands.creation.AccountCategoryCreationCmd;
import com.ia.operation.documents.AccountCategory;
import com.ia.operation.handlers.cmd.AccountCategoryCmdHandler;
import com.ia.operation.handlers.query.AccountCategoryQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.ia.operation.handlers.Handler.CATEGORY_ID_KEY;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

@Component
public class AccountCategoryApiDoc {

    public Consumer<Builder> categoryAdd() {
        return ops -> ops
                .beanClass(AccountCategoryCmdHandler.class)
                .tags(new String[]{"Account categories"})
                .operationId("categoryAdd")
                .beanMethod("categoryAdd")
                .summary("Initiate the account category creation commnand")
                .description("Initiate the account category creation commnand")
                .requestBody(requestBodyBuilder().required(true)
                        .implementation(AccountCategoryCreationCmd.class))
                .response(responseBuilder().responseCode("200")
                        .description("Category creation command accepted.")
                        .implementation(AccountCategory.class));
    }

    public Consumer<Builder> categoryDelete() {
        return ops -> ops
                .beanClass(AccountCategoryCmdHandler.class)
                .tags(new String[]{"Account categories"})
                .operationId("categoryDelete")
                .beanMethod("categoryDelete")
                .summary("Initiate the account category deletion commnand")
                .description("Initiate the account category deletion commnand")
                .parameter(parameterBuilder().description("category identifier")
                        .name(CATEGORY_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200")
                        .description("Category creation command accepted.")
                        .implementation(AccountCategory.class));
    }

    public Consumer<Builder> categoryUpdate() {
        return ops -> ops
                .beanClass(AccountCategoryCmdHandler.class)
                .tags(new String[]{"Account categories"})
                .operationId("categoryUpdate")
                .beanMethod("categoryUpdate")
                .summary("Initiate the account category update commnand")
                .description("Initiate the account category update commnand")
                .requestBody(requestBodyBuilder().required(true)
                        .implementation(AccountCategoryCreationCmd.class))
                .parameter(parameterBuilder().description("category identifier")
                        .name(CATEGORY_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200")
                        .description("Category creation command accepted.")
                        .implementation(AccountCategory.class));
    }

    public Consumer<Builder> accountCategoryGet() {
        return ops -> ops
                .beanClass(AccountCategoryQueryHandler.class)
                .tags(new String[]{"Account categories"})
                .operationId("accountCategoryGet")
                .beanMethod("accountCategoryGet")
                .summary("Retrieve an existing account category.")
                .description("Retrieve an existing account category.")
                .parameter(parameterBuilder().description("category identifier")
                        .name(CATEGORY_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("200")
                        .description("Category successfully retrieved.")
                        .implementation(AccountCategory.class));
    }

    public Consumer<Builder> accountCategoryGetAll() {
        return ops -> ops
                .beanClass(AccountCategoryQueryHandler.class)
                .tags(new String[]{"Account categories"})
                .operationId("accountCategoryGetAll")
                .beanMethod("accountCategoryGetAll")
                .summary("Retrieve an existing account categories.")
                .description("Retrieve an existing account categories.")
                .response(responseBuilder().responseCode("200")
                        .description("Categories successfully retrieved.")
                        .implementationArray(AccountCategory.class));
    }
}
