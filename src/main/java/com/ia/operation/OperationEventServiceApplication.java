package com.ia.operation;

import java.time.LocalDate;
import java.time.Month;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OperationEventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperationEventServiceApplication.class, args);
    }

    @PostConstruct
    public void test() {
        final LocalDate date = LocalDate.now();
        System.out.println(date.withDayOfMonth(1).withMonth(Month.JANUARY.getValue()));
        System.out.println(date.withDayOfMonth(31).withMonth(Month.DECEMBER.getValue()));
    }
}
