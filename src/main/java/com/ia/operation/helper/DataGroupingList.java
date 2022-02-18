package com.ia.operation.helper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.common.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Produce three collections from the provided list base on the provided grouping key.
 * 1- DataGroupingList#duplicates() -> duplicates items of the list.
 * 2- DataGroupingList#uniques() -> unique items of the list.
 * 3- DataGroupingList#itemByKey -> items grouped by the provided key.
 *
 * @param <K> Type of the grouping analyzer key.
 * @param <E> Type of the element in the list.
 * @author Martin Blaise Signe
 * An instance should be ceate using the factory operation of().
 */
@Builder
public final class DataGroupingList<K, E> extends ArrayList<E> {

    private final Predicate<List<E>> multiplePredicate = l -> l.size() > 1;
    private final Predicate<List<E>> singlePredicate = l -> l.size() == 1;

    @Setter
    @Getter
    private Function<E, K> keyProvider;

    private DataGroupingList(Collection<E> items, Function<E, K> keyProvider) {
        super(items);
        this.keyProvider = keyProvider;
    }

    /**
     * Factory operation for instance creation.
     *
     * @param items       initial collection items to analyze. shouldn't be null.
     * @param keyProvider grouping key function. shouldn't be null.
     * @param <U>         grouping key type.
     * @param <V>         item type.
     * @return an instance of DataGroupingHelper object.
     * @throws NullPointerException
     */
    public static <U, V> DataGroupingList<U, V> of(Collection<V> items, Function<V, U> keyProvider) throws NullPointerException {
        Assert.nonNull(items, () -> "items shouldn't be null");
        Assert.nonNull(keyProvider, () -> "keyProvider shouldn't be null");
        return new DataGroupingList<>(items, keyProvider);
    }


    /**
     * Factory operation for instance creation with empty items
     *
     * @param keyProvider grouping key function. shouldn't be null.
     * @param <U>         grouping key type.
     * @param <V>         item type.
     * @return an instance of DataGroupingHelper object.
     * @throws NullPointerException
     */
    public static <U, V> DataGroupingList<U, V> of(Function<V, U> keyProvider) throws NullPointerException {
        Assert.nonNull(keyProvider, () -> "keyProvider shouldn't be null");
        return new DataGroupingList<>(new ArrayList<>(), keyProvider);
    }

    /**
     * @return Map that contain all duplicates according to the grouping key function.
     */
    public Map<K, List<E>> duplicates() {
        return getBranch(multiplePredicate).stream().collect(Collectors.groupingBy(keyProvider));
    }

    /**
     * @return List of all unique item in the list according to the grouping key function.
     */
    public List<E> uniques() {
        return getBranch(singlePredicate);
    }

    /**
     * @return the provided items grouped the provided key.
     */
    public Map<K, List<E>> itemsByKey() {
        return this.stream().collect(Collectors.groupingBy(keyProvider));
    }

    /**
     * @return true if the provided collection contain duplicate items according the grouping key function or false otherwise.
     */
    public boolean hasDuplicates() {
        return match(multiplePredicate);
    }

    /**
     * @return true if the provided collection contain unique items according the grouping key function or false otherwise.
     */
    public boolean hasUniques() {
        return match(singlePredicate);
    }

    public boolean hasSingleUnique() {
        return uniques().size() == 1;
    }

    private List<E> getBranch(Predicate<List<E>> filter) {
        return streamFromMap().filter(filter).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private boolean match(Predicate<List<E>> matchPredicate) {
        return streamFromMap().anyMatch(matchPredicate);
    }

    private Stream<List<E>> streamFromMap() {
        return itemsByKey().values().stream();
    }
}
