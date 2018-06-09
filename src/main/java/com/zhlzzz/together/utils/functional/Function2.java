package com.zhlzzz.together.utils.functional;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Function2<T1, T2, R> {

    R apply(T1 t1, T2 t2);

    default <V> Function2<T1, T2, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T1 t1, T2 t2) -> after.apply(apply(t1, t2));
    }
}
