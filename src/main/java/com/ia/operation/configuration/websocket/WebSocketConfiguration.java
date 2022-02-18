package com.ia.operation.configuration.websocket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.ia.operation.documents.ClientInfo;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Configuration
@Slf4j
@EnableScheduling
public class WebSocketConfiguration {

    @Bean
    public SocketIOServer socketIOServer(WebSocketProperties properties) {
        return new SocketIOServer(properties.getConfig());
    }

    @Autowired
    public void socketInit(SocketIOServer server, ClientInfoRepository repository) {
        server.addConnectListener(client -> {
            log.info("Client {} connected at {}", client.getSessionId(), ZonedDateTime.now());
            client.sendEvent("time_msg", "ZonedDateTime.now()");
        });
        server.addDisconnectListener(client -> {
            repository.findById(client.getSessionId().toString()).ifPresent(c -> {
                c.setConnected(false);
                repository.saveAndFlush(c);
            });
            log.info("Client {} disconnected at {}", client.getSessionId(), ZonedDateTime.now());
            log.info("remaining clients : {}", repository.findAllByConnected(true));
        });
        server.addEventListener("register", RegisterItem.class, (client, data, ackSender) -> {
            log.info("Register event received. from: {}", data.getUserId());
            HandshakeData handshakeData = client.getHandshakeData();
            repository.save(new ClientInfo(client.getSessionId().toString(), data.getUserId(), true));
            log.info("Current clients list: {}", repository.findAllByConnected(true));
            final String message = String.format("%s successfully register with sessionId %s", data.getUserId(), client.getSessionId());
            client.sendEvent(WebSocketEvents.REGISTER.name(), message);
        });
        server.start();
    }

    @Autowired
    private SocketService service;

    @Scheduled(fixedDelay = 10000)
    public void test() {
        WeekFields wf = WeekFields.of(Locale.getDefault());
        System.out.println(LocalDate.now().get(wf.weekOfYear()));
        System.out.println(LocalDate.now().get(wf.weekBasedYear()));
        System.out.println(LocalDate.now().get(wf.weekOfWeekBasedYear()));
        System.out.println(wf.getFirstDayOfWeek());
        final Collection<SocketIOClient> clients = service.getServer().getAllClients();
        log.info("{} clients connected", clients.size());
        if (clients.size() > 0) {
            log.info("broadcasting test message.,");
            //service.broadcast(WebSocketEvents.time_msg, "test message");
            clients.forEach(c->service.send(c,WebSocketEvents.time_msg, "test message"));
        }
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
        private String origin;

        public com.corundumstudio.socketio.Configuration getConfig() {
            final com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
            config.setHostname(hostname);
            config.setPort(port);
            //config.setOrigin(origin);
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
