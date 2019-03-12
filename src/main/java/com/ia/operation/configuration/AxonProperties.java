package com.ia.operation.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "axon.events")
@Data
@Component
public class AxonProperties {
    private String database = "event-database";
    private int snapshotLimit = 3;
    private String classPathPackage = "com.ia";
    private String projectionEventQueue = "projection-event-queue";
    private String projectionCmdQueue = "projection-cmd-queue";
    private String defaultExchange = "default-exchange";
    private String defaultEventRoutingKey = "event";
    private String defaultCmdRoutingKey = "cmd";
}
