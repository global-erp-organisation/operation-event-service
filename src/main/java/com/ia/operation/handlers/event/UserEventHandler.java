package com.ia.operation.handlers.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.ia.operation.documents.User;
import com.ia.operation.events.created.UserCreatedEvent;
import com.ia.operation.events.deleted.UserDeletedEvent;
import com.ia.operation.events.updated.UserUpdatedEvent;
import com.ia.operation.repositories.CompanyRepository;
import com.ia.operation.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Component
@Slf4j
@ProcessingGroup(value = "user-handler")
public class UserEventHandler {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @EventHandler
    public void on(UserCreatedEvent event) {
        log.info("event recieved: [{}]", event);
        companyRepository.findById(event.getCompanyId()).subscribe(company -> {
            userRepository.save(User.of(event, company)).subscribe(u -> {
                log.info("User succesfully saved: [{}]", u);
            });
        });
    }

    @EventHandler
    public void on(UserUpdatedEvent event) {
        log.info("event recieved: [{}]", event);
        companyRepository.findById(event.getCompanyId()).subscribe(company -> {
            userRepository.save(User.of(event, company)).subscribe(u -> {
                log.info("User succesfully updated: [{}]", u);
            });
        });
    }

    @EventHandler
    public void on(UserDeletedEvent event) {
        log.info("event recieved: [{}]", event);
        userRepository.deleteById(event.getUserId()).subscribe(u -> {
            log.info("User succesfully removed: [{}]", event.getUserId());
        });
    }

}
