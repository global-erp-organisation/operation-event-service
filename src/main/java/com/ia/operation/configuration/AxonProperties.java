package com.ia.operation.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "axon.events")
@Data
@Component
public class AxonProperties {
    private String database;
    private int snapshotLimit = 3;
    private String classPathPackage = "com.ia";
}
