package com.ia.operation.helper.history;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.MonthlyHistoryView;
import com.ia.operation.helper.ObjectIdHelper;
import com.ia.operation.repositories.MonthlyHistoryRepository;

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
            final MonthlyHistoryView current = MonthlyHistoryView.from(event).id(ObjectIdHelper.id()).build();
            complete(event, current);
        }).subscribe(current -> {
            current.setCurAmount(current.getCurAmount().add(event.getAmount()));
            complete(event, current);
        });
    }

    private void onUpdate(Operation event, Operation old) {
        final Flux<MonthlyHistoryView> olds = retrieve((getMonth(old.getOperationDate())), old.getAccount().getId());
        olds.switchIfEmpty(a -> onAdd(event)).subscribe(o -> {
            if (isEqual(event, old)) {
                o.setCurAmount(o.getCurAmount().subtract(old.getAmount()).add(event.getAmount()));
                updateHistoryReference(event.getOperationDate().plusMonths(1), event.getAccount().getId(), o);
            } else {
                o.setCurAmount(o.getCurAmount().subtract(old.getAmount()));
                updateHistoryReference(event.getOperationDate().plusMonths(1), old.getAccount().getId(), o);
                final Flux<MonthlyHistoryView> hs = retrieve(getMonth(old.getOperationDate()), event.getAccount().getId());
                hs.switchIfEmpty(a -> onAdd(event)).subscribe(h -> {
                    h.setCurAmount(h.getCurAmount().add(event.getAmount()));
                    updateHistoryReference(event.getOperationDate().plusMonths(1), event.getAccount().getId(), h);
                });
            }
        });
    }

    private void updateHistoryReference(LocalDate date, String accountId, MonthlyHistoryView ref) {
        final Flux<MonthlyHistoryView> olds = retrieve(getMonth(date), accountId);
        olds.subscribe(o -> {
            o.setRefAmount(ref.getCurAmount());
            register(o, ref);
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
        previous.switchIfEmpty(a -> updateViews(current, current, next)).subscribe(p -> {
            current.setRefAmount(p.getCurAmount());
            updateViews(current, p, next);
        });
    }

    private String getMonth(LocalDate date) {
        return date.getMonth() + "-" + date.getYear();
    }

    private void updateViews(MonthlyHistoryView current, MonthlyHistoryView previous, Flux<MonthlyHistoryView> nexts) {
        nexts.switchIfEmpty(a -> register(current)).subscribe(next -> {
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

    @Override
    public boolean isEqual(Operation current, Operation old) {
        return getMonth(current.getOperationDate()).equals(getMonth(old.getOperationDate())) && current.getAccount().getId().equals(old.getAccount().getId());
    }

    @Override
    public  PeriodParams getPeriodParams(LocalDate current) {
        return null;
    }
}
