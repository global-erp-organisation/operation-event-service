package com.ia.operation.util.history;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Realisation;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.repositories.MonthlyHistoryRepository;
import com.ia.operation.repositories.RealisationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
@Slf4j
public class MonthlyHistoryUpdater implements HistoryUpdater<MonthlyHistoryView> {
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final RealisationRepository realisationRepository;

    @Override
    public void update(Realisation event) {
        final String prev = event.getOperationDate().getMonth() + "-" + event.getOperationDate().minusYears(1).getYear();
        final Flux<MonthlyHistoryView> refs = monthlyHistoryRepository.findByMonthAndType(prev.toUpperCase(), event.getOperation().getOperationType());
        final Flux<MonthlyHistoryView> histories = convert(
                realisationRepository.findByPeriod_descriptionAndOperation_operationType(event.getPeriod().getDescription(), event.getOperation().getOperationType()),
                (r) -> MonthlyHistoryView.from(r).build());
        // monthlyHistoryRepository.findByMonthAndType(event.getPeriod().getDescription().toUpperCase(), event.getOperation().getOperationType());

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
                                history.stream().filter(r -> r.getType().equals(event.getOperation().getOperationType())).findFirst().orElse(null);
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

    private Boolean isMatch(MonthlyHistoryView r, Realisation event) {
        return (r.getKey().equals(event.getOperationDate().getMonth()));
    }

}
