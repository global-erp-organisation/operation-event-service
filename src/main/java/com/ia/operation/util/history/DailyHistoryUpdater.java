package com.ia.operation.util.history;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Realisation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.repositories.HistoryByDateRepository;
import com.ia.operation.repositories.RealisationRepository;

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
    private final RealisationRepository realisationRepository;

    @Override
    public void update(Realisation event) {
        final Flux<DailyHistoryView> refs =
                historyByDateRepository.findBydateAndType(event.getOperationDate().minusMonths(1), event.getOperation().getOperationType());
        final Flux<DailyHistoryView> histories = convert(
                realisationRepository.findByOperationDateAndOperation_operationType(event.getOperationDate(), event.getOperation().getOperationType()),
                (r) -> DailyHistoryView.from(r).build());
        // historyByDateRepository.findBydateAndType(event.getOperationDate(), event.getOperation().getOperationType());
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
                                history.stream().filter(r -> r.getType().equals(event.getOperation().getOperationType())).findFirst().orElse(null);
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

    private Boolean isMatch(DailyHistoryView r, Realisation event) {
        return (r.getDate().getDayOfMonth() == event.getOperationDate().getDayOfMonth());
    }
}
