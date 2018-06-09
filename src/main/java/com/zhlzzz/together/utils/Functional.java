package com.zhlzzz.together.utils;

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@UtilityClass
public class Functional {
    public static <T, R> R nullOrMap(@Nullable T nullable, Throwing.Function<T, R> mapper) throws Throwable {
        if (nullable == null) {
            return null;
        } else {
            return mapper.apply(nullable);
        }
    }

    public static <T, R> R nullOrMap(Supplier<T> supplier, Throwing.Function<T, R> mapper) throws Throwable  {
        T nullable = supplier.get();
        if (nullable == null) {
            return null;
        } else {
            return mapper.apply(nullable);
        }
    }

    public static <T> Supplier<T> nullOnThrow(Throwing.Supplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                return null;
            }
        };
    }

    public static <T, R> Function<T, R> nullOnThrow(Throwing.Function<T, R> function) {
        return (t) -> {
            try {
                return function.apply(t);
            } catch (Throwable e) {
                return null;
            }
        };
    }

    public static <T> void setIfNotNull(Supplier<T> getter, Consumer<T> setter) {
        T tmp = getter.get();
        if (tmp != null) {
            setter.accept(tmp);
        }
    }

    public static <T> void setIfNotEmpty(Supplier<T> getter, Consumer<T> setter) {
        T tmp = getter.get();
        if (!isEmpty(tmp)) {
            setter.accept(tmp);
        }
    }

    public static <T> boolean isEmpty(T obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Number) {
            return obj.equals(0);
        } else if (obj instanceof String) {
            return ((String) obj).isEmpty();
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Iterable) {
            return !((Iterable) obj).iterator().hasNext();
        }
        return false;
    }
}
