package com.ia.operation.util.history;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.repositories.MonthlyHistoryRepository;
import com.ia.operation.repositories.OperationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
@Slf4j
public class MonthlyHistoryUpdater implements HistoryUpdater<MonthlyHistoryView> {
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final OperationRepository operationRepository;

    @Override
    public void update(Operation event) {
        final String prev = event.getOperationDate().getMonth() + "-" + event.getOperationDate().minusYears(1).getYear();
        final Flux<MonthlyHistoryView> refs = monthlyHistoryRepository.findByMonthAndType(prev.toUpperCase(), event.getAccount().getAccountType());
        final Flux<MonthlyHistoryView> histories = convert(
                operationRepository.findByPeriod_descriptionAndAccount_accountType(event.getPeriod().getDescription(), event.getAccount().getAccountType()),
                (r) -> MonthlyHistoryView.from(r).build());
        // monthlyHistoryRepository.findByMonthAndType(event.getPeriod().getDescription().toUpperCase(), event.getAccount().getOperationType());

        refs.collectList().subscribe(reference -> {
            final Optional<MonthlyHistoryView> ref = reference.stream().filter(r -> isMatch(r, event)).findFirst();
            histories.collectList().subscribe(history -> {
                if (ref.isPresent()) {
                    if (history.isEmpty()) {
                        monthlyHistoryRepository.save(MonthlyHistoryView.from(event).refAmount(ref.get().getCurAmount()).build()).subscribe(h -> {
                            log.info("History successfully saved: ", h);
                        });
                    } else {
                        history.stream().filter(r -> isMatch(r, event)).forEach(h -> {
                            h.setRefAmount(ref.get().getCurAmount());
                            h.setCurAmount(h.getCurAmount().add(event.getAmount()));
                            monthlyHistoryRepository.save(h).subscribe(o -> {
                                log.info("History successfully saved: ", o);
                            });
                        });
                    }
                } else {
                    if (history.isEmpty()) {
                        monthlyHistoryRepository.save(MonthlyHistoryView.from(event).build()).subscribe(h -> {
                            log.info("History successfully saved: ", h);
                        });
                    } else {
                        final MonthlyHistoryView v =
                                history.stream().filter(r -> r.getType().equals(event.getAccount().getAccountType())).findFirst().orElse(null);
                        if (v != null) {
                            v.setCurAmount(v.getCurAmount().add(event.getAmount()));
                            monthlyHistoryRepository.save(v).subscribe(o -> {
                                log.info("History successfully saved: ", o);
                            });
                        } else {
                            monthlyHistoryRepository.save(MonthlyHistoryView.from(event).build()).subscribe(h -> {
                                log.info("History successfully saved: ", h);
                            });
                        }
                    }
                }
            });
        });
    }

    private Boolean isMatch(MonthlyHistoryView r, Operation event) {
        return (r.getKey().equals(event.getOperationDate().getMonth()));
    }

}
