package com.ia.operation.commands.creation;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectionGenerateCmd implements Serializable {
     String year;
    @Builder.Default
     String routingKey = "cmd";
}
