package com.zhlzzz.together.data;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Slice<T, I> extends Iterable<T>, Serializable {

    @Nonnull
    SliceIndicator<I> getIndicator();

    @Nullable
    SliceIndicator<I> getNextIndicator();

    @Nonnull
    Integer getItemCount();

    @Nullable
    Integer getTotalCount();

    boolean isEmpty();

    boolean hasNext();

    boolean isFirst();

    boolean isLast();

    @Nonnull
    List<T> getItems();

    @Override
    @Nonnull
    default Iterator<T> iterator() {
        return getItems().iterator();
    }

    <U> Slice<U, I> map(Function<? super T, ? extends U> converter);
    <U> Slice<? extends U, I> mapAll(Function<List<? extends T>, List<? extends U>> converter);
    Slice<T, I> filter(Predicate<? super T> predicate);
    <U> Slice<? extends U, I> stream(Function<Stream<? extends T>, List<? extends U>> converter);
}
