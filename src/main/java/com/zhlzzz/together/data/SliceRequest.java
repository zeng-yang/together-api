package com.zhlzzz.together.data;

import lombok.Getter;

import javax.annotation.Nullable;

public class SliceRequest<T> implements SliceIndicator<T> {

    @Getter
    private T cursor;
    @Getter
    private int limit;
    @Getter
    private Direction direction;

    public SliceRequest(@Nullable T cursor, int limit) {
        this(cursor, limit, Direction.FORWARD);
    }

    public SliceRequest(T cursor, Integer limit, Direction direction) {
        this.cursor = cursor;
        this.limit = limit;
        this.direction = direction;
    }

    @Override
    public SliceIndicator<T> reverse() {
        return new SliceRequest<>(cursor, limit, direction.reverse());
    }

    @Override
    public SliceIndicator<T> next(T nextCursor) {
        return new SliceRequest<>(nextCursor, limit, Direction.BACKWARD);
    }
}
