package com.ia.operation.configuration;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.enums.OperationType;
import com.ia.operation.helper.history.HistoryUpdater;
import com.ia.operation.helper.history.UpdateType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

@Configuration
public class OperationConfiguration {

    @Bean
    public Locale locale() {
        return Locale.US;
    }
    
    
    @Bean
    public WebClient webClient() {
        WebClient.builder().filter((r,n)->{
            HttpHeaders headers = r.headers();
            headers.add("token", "token");
            return  n.exchange(r);
        });
        return WebClient.create("http://localhost:8093");
    }
}
