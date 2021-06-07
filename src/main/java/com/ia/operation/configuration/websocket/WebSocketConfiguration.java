package com.ia.operation.configuration.websocket;

import com.corundumstudio.socketio.SocketIOServer;
import com.ia.operation.enums.WebSocketEvents;
import com.ia.operation.helper.history.DashboardBuilder;
import com.ia.operation.queries.account.DashboardQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSocketConfiguration {
    private final DashboardBuilder builder;
    @Bean
    public SocketIOServer socketIOServer(WebSocketProperties properties) {
        return new SocketIOServer(properties.getConfig());
    }

    @Autowired
    public void socketInit(SocketIOServer server) {
        server.addConnectListener(client -> log.info("Client {} connected at {}", client.getSessionId(), ZonedDateTime.now()));
        server.addDisconnectListener(client -> log.info("Client {} disconnected at {}", client.getSessionId(), ZonedDateTime.now()));
        server.addEventListener(WebSocketEvents.DASHBOARD.name(),
                DashboardQuery.class,
                (client, query, ackSender) -> builder.build(DashboardQuery.from(query))
                        .subscribe(view -> server.getBroadcastOperations().sendEvent(WebSocketEvents.DASHBOARD.name(), view)));
        server.start();
    }
}
