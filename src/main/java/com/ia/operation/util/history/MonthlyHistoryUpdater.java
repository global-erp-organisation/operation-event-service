package com.ia.operation.util.history;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.repositories.MonthlyHistoryRepository;
import com.ia.operation.util.ObjectIdUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class MonthlyHistoryUpdater implements HistoryUpdater<MonthlyHistoryView> {

    private final MonthlyHistoryRepository monthlyHistoryRepository;

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
        final Flux<MonthlyHistoryView> currents = retrieve(getMonth(event.getOperationDate()), event.getAccount().getId());
        currents.switchIfEmpty(a -> {
            final MonthlyHistoryView current = MonthlyHistoryView.from(event).id(ObjectIdUtil.id()).build();
            complete(event, current);
        }).subscribe(current -> {
            current.setCurAmount(current.getCurAmount().add(event.getAmount()));
            complete(event, current);
        });
    }

    private void onUpdate(Operation event, Operation old) {
        final Flux<MonthlyHistoryView> olds = retrieve(getMonth(old.getOperationDate()), old.getAccount().getId());
        olds.subscribe(o -> {
            o.setCurAmount(o.getCurAmount().subtract(old.getAmount()).add(event.getAmount()));
            complete(event, o);
        });
    }

    private void onRemove(Operation event) {
        
        final Flux<MonthlyHistoryView> olds = retrieve(getMonth(event.getOperationDate()), event.getAccount().getId());
        olds.subscribe(o -> {
            o.setCurAmount(o.getCurAmount().subtract(event.getAmount()));
            complete(event, o);
        });
    }

    private void complete(Operation event, MonthlyHistoryView current) {
        final LocalDate pd = event.getOperationDate().minusMonths(1);
        final LocalDate nd = event.getOperationDate().plusMonths(1);
        final String prevMonth = getMonth(pd);
        final String nextMonth = getMonth(nd);
        final Flux<MonthlyHistoryView> next = retrieve(nextMonth, event.getAccount().getId());
        final Flux<MonthlyHistoryView> previous = retrieve(prevMonth, event.getAccount().getId());
        previous.switchIfEmpty(a -> {
            updateViews(current, current, next);
        }).subscribe(p -> {
            current.setRefAmount(p.getCurAmount());
            updateViews(current, p, next);
        });
    }

    private String getMonth(LocalDate date) {
        return date.getMonth() + "-" + date.getYear();
    }

    private void updateViews(MonthlyHistoryView current, MonthlyHistoryView previous, Flux<MonthlyHistoryView> nexts) {
        nexts.switchIfEmpty(a -> {
            register(current);
        }).subscribe(next -> {
            next.setRefAmount(current.getCurAmount());
            register(previous, next, current);
        });
    }

    private Flux<MonthlyHistoryView> retrieve(String month, String accountId) {
        return monthlyHistoryRepository.findByMonthAndAccount_id(month, accountId);
    }

    private void register(MonthlyHistoryView... views) {
        Stream.of(views).forEach(v -> monthlyHistoryRepository.save(v).subscribe());
    }

}
