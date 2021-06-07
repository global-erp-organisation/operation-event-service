package com.ia.operation.configuration.doc;

import com.ia.operation.commands.creation.UserCreationCmd;
import com.ia.operation.commands.delete.UserDeletionCmd;
import com.ia.operation.commands.update.UserUpdateCmd;
import com.ia.operation.documents.User;
import com.ia.operation.handlers.cmd.UserCmdHandler;
import com.ia.operation.handlers.query.UserQueryHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.ia.operation.handlers.Handler.*;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

@Component
public class UserApiDoc {
    public Consumer<Builder> userAdd() {
        return ops -> ops
                .tags(new String[]{"Users"})
                .beanClass(UserCmdHandler.class)
                .operationId("userAdd")
                .beanMethod("userAdd")
                .summary("Initiate the user creation command.")
                .description("Initiate the user creation command.")
                .requestBody(requestBodyBuilder().implementation(UserCreationCmd.class))
                .parameter(parameterBuilder().description("company identifier")
                        .name(COMPANY_ID_KEY)
                        .in(ParameterIn.PATH).required(true))
                .response(responseBuilder().responseCode("201")
                        .description("User creation command accepted.")
                        .implementation(UserCreationCmd.class));

    }

    public Consumer<Builder> userDelete() {
        return ops -> ops
                .tags(new String[]{"Users"})
                .beanClass(UserCmdHandler.class)
                .operationId("userDelete")
                .beanMethod("userDelete")
                .summary("Initiate the user deletion command.")
                .description("Initiate the user deletion command.")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("201")
                        .description("User deletion command accepted.")
                        .implementation(UserDeletionCmd.class));

    }

    public Consumer<Builder> userUpdate() {
        return ops -> ops
                .tags(new String[]{"Users"})
                .beanClass(UserCmdHandler.class)
                .operationId("userUpdate")
                .beanMethod("userUpdate")
                .summary("Initiate the user update command.")
                .description("Initiate the user update command.")
                .requestBody(requestBodyBuilder().implementation(UserCreationCmd.class))
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("201")
                        .description("User update command accepted.")
                        .implementation(UserUpdateCmd.class));
    }

    public Consumer<Builder> userGet() {
        return ops -> ops
                .tags(new String[]{"Users"})
                .beanClass(UserQueryHandler.class)
                .operationId("userGet")
                .beanMethod("userGet")
                .summary("Retrieve an existing user.")
                .description("Retrieve an existing user.")
                .parameter(parameterBuilder().description("user identifier")
                        .name(USER_ID_KEY)
                        .in(ParameterIn.PATH))
                .response(responseBuilder().responseCode("200")
                        .description("User update command accepted.")
                        .implementation(User.class));
    }

    public Consumer<Builder> userGetByEmail() {
        return ops -> ops
                .tags(new String[]{"Users"})
                .beanClass(UserQueryHandler.class)
                .operationId("userGetByEmail")
                .beanMethod("userGetByEmail")
                .summary("Retrieve an existing user by email.")
                .description("Retrieve an existing user by emal.")
                .parameter(parameterBuilder().description("user email")
                        .name(EMAIL_KEY)
                        .in(ParameterIn.QUERY))
                .response(responseBuilder().responseCode("200")
                        .description("User successfully retrieved.")
                        .implementationArray(User.class));
    }
}
