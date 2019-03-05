package com.ia.operation.util.history;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.repositories.HistoryByDateRepository;
import com.ia.operation.repositories.OperationRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Data
@Component
@Slf4j
public class DailyHistoryUpdater implements HistoryUpdater<DailyHistoryView> {

    private final HistoryByDateRepository historyByDateRepository;
    private final OperationRepository operationRepository;

    @Override
    public void update(Operation event) {
        final Flux<DailyHistoryView> refs =
                historyByDateRepository.findBydateAndType(event.getOperationDate().minusMonths(1), event.getAccount().getAccountType());
        final Flux<DailyHistoryView> histories = convert(
                operationRepository.findByOperationDateAndAccount_accountType(event.getOperationDate(), event.getAccount().getAccountType()),
                (r) -> DailyHistoryView.from(r).build());
        refs.collectList().subscribe(reference -> {
            final Optional<DailyHistoryView> ref = reference.stream().filter(r -> isMatch(r, event)).findFirst();
            histories.collectList().subscribe(history -> {
                if (ref.isPresent()) {
                    if (history.isEmpty()) {
                        historyByDateRepository.save(DailyHistoryView.from(event).refAmount(ref.get().getCurAmount()).build()).subscribe(h -> {
                            log.info("History successfully saved: ", h);
                        });
                    } else {
                        history.stream().filter(r -> isMatch(r, event)).forEach(h -> {
                            h.setRefAmount(ref.get().getCurAmount());
                            h.setCurAmount(h.getCurAmount().add(event.getAmount()));
                            historyByDateRepository.save(h).subscribe(o -> {
                                log.info("History successfully saved: ", o);
                            });
                        });
                    }
                } else {
                    if (history.isEmpty()) {
                        historyByDateRepository.save(DailyHistoryView.from(event).build()).subscribe(h -> {
                            log.info("History successfully saved: ", h);
                        });
                    } else {
                        final DailyHistoryView v =
                                history.stream().filter(r -> r.getType().equals(event.getAccount().getAccountType())).findFirst().orElse(null);
                        if (v != null) {

                            v.setCurAmount(v.getCurAmount().add(event.getAmount()));
                            historyByDateRepository.save(v).subscribe(o -> {
                                log.info("History successfully saved: ", o);
                            });
                        } else {
                            historyByDateRepository.save(DailyHistoryView.from(event).build()).subscribe(h -> {
                                log.info("History successfully saved: ", h);
                            });
                        }
                    }
                }
            });
        });
    }

    private Boolean isMatch(DailyHistoryView r, Operation event) {
        return (r.getDate().getDayOfMonth() == event.getOperationDate().getDayOfMonth());
    }
}
