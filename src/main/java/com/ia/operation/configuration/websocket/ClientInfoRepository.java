package com.ia.operation.configuration.websocket;

import com.ia.operation.documents.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, String> {
    List<ClientInfo> findClientInfoByUseridAndConnected(String userId, boolean connected);
    List<ClientInfo> findAllByConnected(boolean connected);
}
