package com.ia.operation.configuration.api.doc;

import com.ia.operation.commands.creation.CompanyCreationCmd;
import com.ia.operation.commands.delete.CompanyDeletionCmd;
import com.ia.operation.commands.update.CompanyUpdateCmd;
import com.ia.operation.documents.Company;
import com.ia.operation.handlers.Handler;
import com.ia.operation.handlers.cmd.CompanyCmdHandler;
import com.ia.operation.handlers.query.CompanyQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

@Component
public class CompanyApiDoc {
    public Consumer<Builder> companyAdd() {
        return ops -> ops.beanClass(CompanyCmdHandler.class)
                .tags(new String[]{"Companies"})
                .operationId("companyAdd")
                .beanMethod("companyAdd")
                .summary("Initiate the company creation command")
                .description("Initiate the company creation command.")
                .requestBody(requestBodyBuilder().description("Company creation body")
                        .implementation(CompanyCreationCmd.class)
                        .required(true))
                .response(responseBuilder().responseCode("201")
                        .description("Company creation command accepted")
                        .implementation(CompanyCreationCmd.class));
    }

    public Consumer<Builder> companyRemove() {
        return ops -> ops.beanClass(CompanyCmdHandler.class)
                .tags(new String[]{"Companies"})
                .operationId("companyRemove")
                .beanMethod("companyRemove")
                .summary("Initiate the company deletion command")
                .description("Initiate the company deletion command.")
                .parameter(parameterBuilder().description("company identifier")
                        .name(Handler.COMPANY_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("201")
                        .description("Company deletion command accepted")
                        .implementation(CompanyDeletionCmd.class));
    }

    public Consumer<Builder> companyUpdate() {
        return ops -> ops.beanClass(CompanyCmdHandler.class)
                .tags(new String[]{"Companies"})
                .operationId("companyUpdate")
                .beanMethod("companyUpdate")
                .summary("Initiate the company update command")
                .description("Initiate the company update command.")
                .parameter(parameterBuilder().description("company identifier")
                        .name(Handler.COMPANY_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("201")
                        .description("Company update command accepted")
                        .implementation(CompanyUpdateCmd.class));
    }

    public Consumer<Builder> companyGet() {
        return ops -> ops
                .tags(new String[]{"Companies"})
                .beanClass(CompanyQueryHandler.class)
                .operationId("companyGet")
                .beanMethod("companyGet")
                .summary("Retrieve and existing company")
                .description("Retrieve and existing company.")
                .parameter(parameterBuilder().description("company identifier")
                        .name(Handler.COMPANY_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("Company successfully retrieved.")
                        .implementation(Company.class));
    }

    public Consumer<Builder> companyGetAll() {

        return ops -> ops.beanClass(CompanyQueryHandler.class)
                .tags(new String[]{"Companies"})
                .operationId("companyGetAll")
                .beanMethod("companyGetAll")
                .summary("Retrieve and existing companies")
                .description("Retrieve and existing companies.")
                .response(responseBuilder().responseCode("200")
                        .description("Companies successfully retrieved.")
                        .implementationArray(Company.class));
    }
}
