package com.ia.operation.configuration.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import com.ia.operation.enums.WebSocketEvents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebSocketConfiguration {
    private static final Map<UUID, String> clients = new ConcurrentHashMap<>();

    @Bean
    public SocketIOServer socketIOServer(WebSocketProperties properties) {
        return new SocketIOServer(properties.getConfig());
    }

    public static Optional<UUID> userIdToSessionId(String userId) {
        return clients.entrySet().stream().filter(e -> e.getValue().equalsIgnoreCase(userId)).findFirst().map(Map.Entry::getKey);
    }

    @Autowired
    public void socketInit(SocketIOServer server) throws  Exception {
        server.addConnectListener(client -> log.info("Client {} connected at {}", client.getSessionId(), ZonedDateTime.now()));
        server.addDisconnectListener(client -> {
            clients.remove(client.getSessionId());
            log.info("Client {} disconnected at {}", client.getSessionId(), ZonedDateTime.now());
            log.info("remaining clients : {}", clients);
        });
        server.addEventListener(WebSocketEvents.REGISTER.name(), RegisterItem.class, (client, data, ackSender) -> {
            log.info("Register event received. from: {}", data.getUserId());
            clients.put(client.getSessionId(), data.getUserId());
            log.info("Current clients list: {}", clients);
        });
        server.start();
    }

    @Component
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    @ConfigurationProperties(prefix = "web-socket")
    public static class WebSocketProperties {
        private String hostname;
        private int port;

        public com.corundumstudio.socketio.Configuration getConfig() {
            final com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
            config.setHostname(hostname);
            config.setPort(port);
            return config;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class RegisterItem {
        private String userId;
    }
}
