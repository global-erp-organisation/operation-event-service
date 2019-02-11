package com.ia.operation;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.Function;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class OperationEventServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(OperationEventServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final int n = 10;
 
        System.out.println("imperative strategy.");
        System.out.println("--------------------.");
        final Long imperativeTime = compute(n, (number) -> {
            int sum = 0;
            for (int i = 1; i <= number; i++) {
                if (number % i == 0) {
                    sum += i;
                }
            }
            log.info("The sum of divisor for {} is {}", n, sum);
            return System.currentTimeMillis();
        });
        log.info("Imperative time in millis is {}", imperativeTime);
        
        System.out.println("functional strategy.");
        System.out.println("--------------------.");
        final Long functionnalTime = compute(n, (number) -> {
            log.info("The sum of divisor for {} is {}", n, sumOfDivisor(n));
            return System.currentTimeMillis();
        });
        log.info("Functionnal time in millis is {}", functionnalTime);
    }

    private int sumOfDivisor(int n) {
        return IntStream.range(1, n + 1).filter(i -> n % i == 0).reduce((a, b) -> a + b).getAsInt();
    }

    private Long compute(int n, Function<Integer, Long> func) {
        final Long start = System.currentTimeMillis();
        final Long end = func.apply(new Integer(n));
        return end - start;
    }
}
