package com.ia.operation.helper.history;

import com.ia.operation.documents.Operation;
import com.ia.operation.documents.views.DailyHistoryView;
import com.ia.operation.helper.ObjectIdHelper;
import com.ia.operation.repositories.DailyHistoryRepository;
import com.ia.operation.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Data
@Component
public class DailyHistoryUpdater implements HistoryUpdater<DailyHistoryView, Operation> {

    private static final int ONE_FACTOR = 1;
    private final DailyHistoryRepository dailyHistoryRepository;
    private final OperationRepository operationRepository;

    @Override
    public void update(Operation event, Operation old, UpdateType type) {
        proceed(event, old, type);
    }

    @Override
    public void onAdd(Operation event) {
        final Flux<DailyHistoryView> currents = retrieve(event.getOperationDate(), event.getAccount().getId());
        currents.switchIfEmpty(a -> {
            final DailyHistoryView current = DailyHistoryView.from(event).id(ObjectIdHelper.id()).build();
            complete(event, current);
        }).subscribe(current -> {
            current.setCurAmount(current.getCurAmount().add(event.getAmount()));
            complete(event, current);
        });
    }

    @Override
    public void onUpdate(Operation event, Operation old) {
        final Flux<DailyHistoryView> olds = retrieve(old.getOperationDate(), old.getAccount().getId());
        olds.switchIfEmpty(a -> onAdd(event)).subscribe(o -> {
            if (isEqual(event, old)) {
                o.setCurAmount(o.getCurAmount().subtract(old.getAmount()).add(event.getAmount()));
                updateHistoryReference(event.getOperationDate().plusDays(ONE_FACTOR), event.getAccount().getId(), o);
            } else {
                o.setCurAmount(o.getCurAmount().subtract(old.getAmount()));
                updateHistoryReference(event.getOperationDate().plusDays(ONE_FACTOR), old.getAccount().getId(), o);
                final Flux<DailyHistoryView> hs = retrieve(old.getOperationDate(), event.getAccount().getId());
                hs.switchIfEmpty(a -> onAdd(event)).subscribe(h -> {
                    h.setCurAmount(h.getCurAmount().add(event.getAmount()));
                    updateHistoryReference(event.getOperationDate().plusDays(ONE_FACTOR), event.getAccount().getId(), h);
                });
            }
        });
    }

    private void updateHistoryReference(LocalDate date, String accountId, DailyHistoryView ref) {
        final Flux<DailyHistoryView> olds = retrieve(date, accountId);
        olds.subscribe(o -> {
            o.setRefAmount(ref.getCurAmount());
            register(o, ref);
        });
    }

    @Override
    public void onRemove(Operation event) {
        final Flux<DailyHistoryView> olds = retrieve(event.getOperationDate(), event.getAccount().getId());
        olds.subscribe(o -> {
            o.setCurAmount(o.getCurAmount().subtract(event.getAmount()));
            complete(event, o);
        });
    }

    private void complete(Operation event, DailyHistoryView current) {
        final Flux<DailyHistoryView> next = retrieve(event.getOperationDate().plusDays(ONE_FACTOR), event.getAccount().getId());
        final Flux<DailyHistoryView> previous = retrieve(event.getOperationDate().minusDays(ONE_FACTOR), event.getAccount().getId());
        previous.switchIfEmpty(a -> updateViews(current, current, next)).subscribe(p -> {
            current.setRefAmount(p.getCurAmount());
            updateViews(current, p, next);
        });
    }

    private void updateViews(DailyHistoryView current, DailyHistoryView previous, Flux<DailyHistoryView> nexts) {
        nexts.switchIfEmpty(a -> register(current)).subscribe(next -> {
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

    @Override
    public boolean isEqual(Operation current, Operation old) {
        return current.getAccount().getId().equals(old.getAccount().getId()) && current.getOperationDate().equals(old.getOperationDate());
    }

    @Override
    public PeriodParams getPeriodParams(LocalDate current) {
        //TODO provided the collections of date that need to be sorted
        final List<LocalDate> dates = new ArrayList<LocalDate>().stream().sorted().collect(Collectors.toList());
        final PeriodParams.PeriodParamsBuilder params = PeriodParams.builder();
        if (dates.isEmpty()) {
            params.previous(current.minusDays(ONE_FACTOR)).next(current.plusDays(ONE_FACTOR));
        }
        if (dates.size() == 1) {
            final LocalDate date = dates.get(0);
            if (date.equals(current)) {
                params.previous(current.minusDays(ONE_FACTOR)).next(current.plusDays(ONE_FACTOR)).build();
            } else if (date.isBefore(current)) {
                params.previous(date).next(current.plusDays(ONE_FACTOR));
            } else
                params.previous(current.minusDays(ONE_FACTOR)).next(date);
        }
        if (dates.size() == 2) {
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(current)) {
                    if (i == 0) {
                         params.previous(current.minusDays(ONE_FACTOR)).next(dates.get(i + 1));
                    }
                     params.previous(dates.get(i - 1)).next(current.plusDays(ONE_FACTOR));
                }
            }
        } else {
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(current)) {
                    if (i == 0) {
                        return params.previous(current.minusDays(ONE_FACTOR)).next(dates.get(i + 1)).build();
                    }
                    return params.previous(dates.get(i - 1)).next(dates.get(i + 1)).build();
                }
            }
        }
        return params.build();
    }
}
