package com.zhlzzz.together.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SliceImpl<T, I> implements Slice<T, I> {

    private SliceIndicator<I> currentIndicator;
    private SliceIndicator<I> nextIndicator;
    private List<T> items;
    private Integer totalCount;

    public SliceImpl(@Nonnull SliceIndicator<I> indicator, List<T> items, @Nullable I nextCursor, @Nullable Integer totalCount) {
        currentIndicator = indicator;
        nextIndicator = nextCursor == null ? null : indicator.next(nextCursor);
        this.items = items;
        this.totalCount = totalCount;
    }

    @Nonnull
    @Override
    public SliceIndicator<I> getIndicator() {
        return currentIndicator;
    }

    @Nullable
    @Override
    public SliceIndicator<I> getNextIndicator() {
        return nextIndicator;
    }

    @Nonnull
    @Override
    public Integer getItemCount() {
        return items.size();
    }

    @Nullable
    @Override
    public Integer getTotalCount() {
        return totalCount;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean hasNext() {
        return nextIndicator != null;
    }

    @Override
    public boolean isFirst() {
        return currentIndicator.getCursor() == null;
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Nonnull
    @Override
    public List<T> getItems() {
        return items;
    }

    @Override
    public <U> Slice<U, I> map(Function<? super T, ? extends U> converter) {
        List<U> mappedItems = items.stream().map(converter).collect(Collectors.toList());
        return new SliceImpl<>(currentIndicator, mappedItems, nextIndicator == null ? null : nextIndicator.getCursor(), totalCount);
    }

    @Override
    public <U> Slice<? extends U, I> mapAll(Function<List<? extends T>, List<? extends U>> converter) {
        List<? extends U> mappedItems = converter.apply(items);
        return new SliceImpl<>(currentIndicator, mappedItems, nextIndicator == null ? null : nextIndicator.getCursor(), totalCount);
    }

    @Override
    public Slice<T, I> filter(Predicate<? super T> predicate) {
        List<T> filteredItems = items.stream().filter(predicate).collect(Collectors.toList());
        return new SliceImpl<>(currentIndicator, filteredItems, nextIndicator == null ? null : nextIndicator.getCursor(), totalCount);
    }

    @Override
    public <U> Slice<? extends U, I> stream(Function<Stream<? extends T>, List<? extends U>> converter) {
        List<? extends U> streamedItems = converter.apply(items.stream());
        return new SliceImpl<>(currentIndicator, streamedItems, nextIndicator == null ? null : nextIndicator.getCursor(), totalCount);
    }
}
