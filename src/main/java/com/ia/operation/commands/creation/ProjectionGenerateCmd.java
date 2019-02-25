package com.ia.operation.commands.creation;

import java.io.Serializable;

import lombok.Builder;
import lombok.Value;

@SuppressWarnings("serial")
@Value
@Builder
public class ProjectionGenerateCmd implements Serializable {
    private String year;
    @Builder.Default
    private String routingKey = "cmd";
}
