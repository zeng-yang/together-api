package com.zhlzzz.together.utils;


import com.zhlzzz.together.utils.functional.Consumer3;
import com.zhlzzz.together.utils.functional.Function2;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@UtilityClass
@SuppressWarnings("unused")
public class CollectionUtils {

    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static <T, R> List<R> map(@Nullable List<T> l, Function<T, R> mapper) {
        if (l == null) {
            return null;
        }
        return l.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T, R> Set<R> map(@Nullable Set<T> s, Function<T, R> mapper) {
        if (s == null) {
            return null;
        }
        return s.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <T, K> Map<K, T> toMap(@Nullable List<T> l, Function2<T, Integer, K> keyMapper) {
        if (l == null) {
            return null;
        }
        Map<K, T> map = new HashMap<>();
        forEach(l, (v, i)->{
            map.put(keyMapper.apply(v, i), v);
        });
        return map;
    }

    public static <K, V> Map<K, V> toMap(@Nullable Set<V> s, Function<V, K> keyMapper) {
        if (s == null) {
            return null;
        }
        Map<K, V> map = new HashMap<>();
        s.forEach((v)->map.put(keyMapper.apply(v), v));
        return map;
    }

    public static <T, TI extends Iterable<? extends T>> Optional<T> find(@Nullable TI iterable, Predicate<T> predicate) {
        if (iterable == null) {
            return Optional.empty();
        }
        for (T cur : iterable) {
            if (predicate.test(cur)) {
                return Optional.ofNullable(cur);
            }
        }
        return Optional.empty();
    }

    public static <T> void forEach(@Nullable List<T> l, BiConsumer<T, Integer> consumer) {
        if (l == null) {
            return;
        }
        Iterator<T> itor = l.iterator();
        int i = 0;
        while (itor.hasNext()) {
            consumer.accept(itor.next(), i);
            i += 1;
        }
    }

    public static <T> void add(@Nullable Collection<T> collection, T element) {
        if (collection != null) {
            collection.add(element);
        }
    }

    public static <N, NI extends Iterable<? extends N>> void walkTree(@Nullable N parent, Function<N, NI> childrenSupplier, boolean deepFirst, Consumer<N> consumer) {
        walkTree(parent, childrenSupplier, deepFirst, (n, p, d)->consumer.accept(n));
    }

    public static <N, NI extends Iterable<? extends N>> void walkTree(@Nullable N parent, Function<N, NI> childrenSupplier, boolean deepFirst, BiConsumer<N, N> consumer) {
        walkTree(parent, childrenSupplier, deepFirst, (n, p, d)->consumer.accept(n, p));
    }

    public static <N, NI extends Iterable<? extends N>> void walkTree(@Nullable N parent, Function<N, NI> childrenSupplier, boolean deepFirst, Consumer3<N, N, Integer> consumer) {
        if (parent == null) {
            return;
        }
        consumer.accept(parent, null, 0);
        NI children = childrenSupplier.apply(parent);

        doWalkTrees(children, parent, childrenSupplier, deepFirst, 1, consumer);
    }

    public static <N, NI extends Iterable<? extends N>> void walkTrees(@Nullable NI trees, Function<N, NI> childrenSupplier, boolean deepFirst, Consumer<N> consumer) {
        doWalkTrees(trees, null, childrenSupplier, deepFirst, 0, (n, p, d)->consumer.accept(n));
    }

    public static <N, NI extends Iterable<? extends N>> void walkTrees(@Nullable NI trees, Function<N, NI> childrenSupplier, boolean deepFirst, BiConsumer<N, N> consumer) {
        doWalkTrees(trees, null, childrenSupplier, deepFirst, 0, (n, p, d)->consumer.accept(n, p));
    }

    public static <N, NI extends Iterable<? extends N>> void walkTrees(@Nullable NI trees, Function<N, NI> childrenSupplier, boolean deepFirst, Consumer3<N, N, Integer> consumer) {
        doWalkTrees(trees, null, childrenSupplier, deepFirst, 0, consumer);
    }

    private static <N, NI extends Iterable<? extends N>> void doWalkTrees(@Nullable NI trees, N parent, Function<N, NI> childrenSupplier, boolean deepFirst, Integer deep, Consumer3<N, N, Integer> consumer) {
        if (trees == null) {
            return;
        }
        Iterator<? extends N> itor = trees.iterator();
        while (itor.hasNext()) {
            N current = itor.next();
            consumer.accept(current, parent, deep);
            if (deepFirst) {
                doWalkTrees(childrenSupplier.apply(current), current, childrenSupplier, true, deep + 1, consumer);
            }
        }

        if (!deepFirst) {
            Iterator<? extends N> itorAgain = trees.iterator();
            while (itorAgain.hasNext()) {
                N current = itor.next();
                doWalkTrees(childrenSupplier.apply(current), current, childrenSupplier, false, deep + 1, consumer);
            }
        }
    }
}
