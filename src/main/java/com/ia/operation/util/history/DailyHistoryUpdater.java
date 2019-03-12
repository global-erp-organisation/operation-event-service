package com.ia.operation.util.history;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.repositories.DailyHistoryRepository;
import com.ia.operation.repositories.OperationRepository;
import com.ia.operation.util.ObjectIdUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.Disposable;
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

    private Disposable onAdd(Operation event) {
        final Flux<DailyHistoryView> currents = retrieve(event.getOperationDate(), event.getAccount().getId());
        return currents.switchIfEmpty(a -> {
            final DailyHistoryView current = DailyHistoryView.from(event).id(ObjectIdUtil.id()).build();
            complete(event, current);
        }).subscribe(current -> {
            current.setCurAmount(current.getCurAmount().add(event.getAmount()));
            complete(event, current);
        });
    }

    private void onUpdate(Operation event, Operation old) {

        final Flux<DailyHistoryView> olds = retrieve(old.getOperationDate(), old.getAccount().getId());
        final Flux<DailyHistoryView> currents = retrieve(event.getOperationDate(), event.getAccount().getId());
        olds.subscribe(o -> {
            currents.switchIfEmpty(a -> {
                o.setCurAmount(o.getCurAmount().subtract(old.getAmount()));
                complete(old, o);
                final DailyHistoryView view = DailyHistoryView.from(event).id(ObjectIdUtil.id()).build();
                complete(event, view);
            }).subscribe(c -> {
                o.setCurAmount(o.getCurAmount().subtract(old.getAmount()).add(event.getAmount()));
                complete(event, o);
            });
        });
    }

    private void onRemove(Operation event) {
        final Flux<DailyHistoryView> olds = retrieve(event.getOperationDate(), event.getAccount().getId());
        olds.subscribe(o -> {
            o.setCurAmount(o.getCurAmount().subtract(event.getAmount()));
            complete(event, o);
        });
    }

    private void complete(Operation event, DailyHistoryView current) {
        final Flux<DailyHistoryView> next = retrieve(event.getOperationDate().plusDays(1), event.getAccount().getId());
        final Flux<DailyHistoryView> previous = retrieve(event.getOperationDate().minusDays(1), event.getAccount().getId());
        previous.switchIfEmpty(a -> {
            updateViews(current, current, next);
        }).subscribe(p -> {
            current.setRefAmount(p.getCurAmount());
            updateViews(current, p, next);
        });
    }

    private void updateViews(DailyHistoryView current, DailyHistoryView previous, Flux<DailyHistoryView> nexts) {
        nexts.switchIfEmpty(a -> {
            register(current);
        }).subscribe(next -> {
            next.setRefAmount(current.getCurAmount());
            register(previous, next, current);
        });
    }

    private Flux<DailyHistoryView> retrieve(LocalDate date, String accountId) {
        return dailyHistoryRepository.findBydateAndAccount_id(date, accountId);
    }

    private void register(DailyHistoryView... views) {
        Stream.of(views).forEach(v -> dailyHistoryRepository.save(v).subscribe());
    }

/*    private boolean isEqual(Operation current, Operation old) {
        return current.getAccount().getId().equals(old.getAccount().getId()) && current.getOperationDate().equals(old.getOperationDate())
                && current.getAmount().equals(old.getAmount());
    }*/
}
