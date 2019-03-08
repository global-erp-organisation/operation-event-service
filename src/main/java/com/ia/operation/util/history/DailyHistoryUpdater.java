package com.ia.operation.util.history;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.repositories.DailyHistoryRepository;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@Data
@Component
public class DailyHistoryUpdater implements HistoryUpdater<DailyHistoryView> {

    private final DailyHistoryRepository dailyHistoryRepository;
    private final OperationRepository operationRepository;

    @Override
    public void update(Operation event, Operation old, UpdateType type) {
        switch (type) {
            case A:
                onAdd(event);
                break;
            case U:
                onUpdate(event, old);
                break;
            case R:
                onRemove(event);
                break;
            default:
                throw new IllegalArgumentException("Unknown update type.");
        }
    }

    private void onAdd(Operation event) {
        final Flux<DailyHistoryView> current = dailyHistoryRepository.findBystartAndAccount_id(event.getOperationDate(), event.getAccount().getId());
        current.switchIfEmpty(a -> {
            final DailyHistoryView view = DailyHistoryView.from(event).curAmount(BigDecimal.ZERO).id(ObjectIdUtil.id()).build();
            view.setCurAmount(view.getCurAmount().add(event.getAmount()));
            complete(event, view);
        }).subscribe(c -> {
            c.setCurAmount(c.getCurAmount().add(event.getAmount()));
            complete(event, c);
        });
    }

    private void onUpdate(Operation event, Operation old) {
        final Flux<DailyHistoryView> olds = retrieve(old.getOperationDate(), old.getAccount().getId());
        olds.subscribe(o -> {
            o.setCurAmount(o.getCurAmount().subtract(old.getAmount()).add(event.getAmount()));
            complete(event, o);
        });
    }

    private void onRemove(Operation event) {
        final Flux<DailyHistoryView> olds = retrieve(event.getOperationDate(), event.getAccount().getId());
        olds.subscribe(o -> {
            o.setCurAmount(o.getCurAmount().subtract(event.getAmount()));
            complete(event, o);
        });
    }

    private void complete(Operation event, DailyHistoryView view) {
        final Flux<DailyHistoryView> next = retrieve(event.getOperationDate().plusDays(1), event.getAccount().getId());
        final Flux<DailyHistoryView> previous = retrieve(event.getOperationDate().minusDays(1), event.getAccount().getId());
        previous.switchIfEmpty(a -> {
            updateViews(view, view, next);
        }).subscribe(p -> {
            view.setRefAmount(p.getCurAmount());
            updateViews(view, p, next);
        });
    }

    private void updateViews(DailyHistoryView view, DailyHistoryView previous, Flux<DailyHistoryView> next) {
        next.switchIfEmpty(a -> {
            dailyHistoryRepository.save(view).subscribe();
        }).subscribe(n -> {
            n.setRefAmount(view.getCurAmount());
            dailyHistoryRepository.save(previous).subscribe();
            dailyHistoryRepository.save(n).subscribe();
            dailyHistoryRepository.save(view).subscribe();
        });
    }

    private Flux<DailyHistoryView> retrieve(LocalDate date, String accountId) {
        return dailyHistoryRepository.findBystartAndAccount_id(date, accountId);
    }
}
