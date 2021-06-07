package com.ia.operation.configuration.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.corundumstudio.socketio.Configuration;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ConfigurationProperties(prefix = "web-socket")
public class WebSocketProperties {
    private String hostname;
    private int port;

    public Configuration getConfig(){
        final Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        return config;
    }
}
