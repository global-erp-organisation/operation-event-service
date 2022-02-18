package com.ia.operation.configuration.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.ia.operation.enums.WebSocketEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

@Component
@Slf4j
public class SocketService {

    private final SocketIOServer server;
    private final ClientInfoRepository repository;

    public SocketService(@Lazy SocketIOServer server, ClientInfoRepository repository) {
        this.server = server;
        this.repository = repository;
    }

    public void notify(String clientId, WebSocketEvents event, Object... data) {
        repository.findClientInfoByUseridAndConnected(clientId, true)
                .forEach(info -> send(info.getSessionId(), event, data));
    }

    public void broadcast(WebSocketEvents event, Object... data) {
        server.getBroadcastOperations().sendEvent(event.name(), data);
    }

    public Optional<SocketIOClient> getClient(String sessionId) {
        final Predicate<SocketIOClient> eligibleClient = s -> s.getSessionId().toString().equals(sessionId) && s.isChannelOpen();
        return server.getAllClients().stream().filter(eligibleClient).findFirst();
    }

    public SocketIOServer getServer() {
        return server;
    }

    public void send(SocketIOClient client, WebSocketEvents eventName, Object... data) {
        client.sendEvent(eventName.name(), data);
    }

    private void send(String sessionId, WebSocketEvents eventName, Object... data) {
        getClient(sessionId).ifPresent(client -> send(client, eventName, data));
    }
}
