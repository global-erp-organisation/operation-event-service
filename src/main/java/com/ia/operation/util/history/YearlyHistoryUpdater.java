package com.ia.operation.util.history;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.YearlyHistoryView;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.repositories.YearlyHistoryRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
@Slf4j
public class YearlyHistoryUpdater implements HistoryUpdater<YearlyHistoryView> {

    private final YearlyHistoryRepository yearlyHistoryRepository;
    private final OperationRepository operationRepository;

    @Override
    public void update(Operation event) {
        final Flux<YearlyHistoryView> refs =
                yearlyHistoryRepository.findByYearAndType(event.getOperationDate().minusYears(1).getYear(), event.getAccount().getOperationType());
        final Flux<YearlyHistoryView> histories =
                convert(operationRepository.findByPeriod_yearAndAccount_operationType(event.getOperationDate().getYear(),
                        event.getAccount().getOperationType()), (r) -> YearlyHistoryView.from(r).build());
        // yearlyHistoryRepository.findByYearAndType(event.getOperationDate().getYear(), event.getAccount().getOperationType());

        refs.collectList().subscribe(reference -> {
            final Optional<YearlyHistoryView> ref = reference.stream().filter(r -> isMatch(r, event)).findFirst();
            histories.collectList().subscribe(history -> {
                if (ref.isPresent()) {
                    if (history.isEmpty()) {
                        yearlyHistoryRepository.save(YearlyHistoryView.from(event).refAmount(ref.get().getCurAmount()).build()).subscribe(h -> {
                            log.info("History successfully saved: ", h);
                        });
                    } else {
                        history.stream().filter(r -> isMatch(r, event)).forEach(h -> {
                            h.setRefAmount(ref.get().getCurAmount());
                            h.setCurAmount(h.getCurAmount().add(event.getAmount()));
                            yearlyHistoryRepository.save(h).subscribe(o -> {
                                log.info("History successfully saved: ", o);
                            });
                        });
                    }
                } else {
                    if (history.isEmpty()) {
                        yearlyHistoryRepository.save(YearlyHistoryView.from(event).build()).subscribe(h -> {
                            log.info("History successfully saved: ", h);
                        });
                    } else {
                        final YearlyHistoryView v =
                                history.stream().filter(r -> r.getType().equals(event.getAccount().getOperationType())).findFirst().orElse(null);
                        if (v != null) {
                            v.setCurAmount(v.getCurAmount().add(event.getAmount()));
                            yearlyHistoryRepository.save(v).subscribe(o -> {
                                log.info("History successfully saved: ", o);
                            });
                        } else {
                            yearlyHistoryRepository.save(YearlyHistoryView.from(event).build()).subscribe(h -> {
                                log.info("History successfully saved: ", h);
                            });
                        }
                    }
                }
            });
        });
    }

    private Boolean isMatch(YearlyHistoryView r, Operation event) {
        return (r.getYear() == event.getOperationDate().minusYears(1).getYear());
    }

}
