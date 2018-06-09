package com.zhlzzz.together.data;

import javax.annotation.Nullable;
import java.io.Serializable;

public interface SliceIndicator<T> extends Serializable {

    @Nullable
    T getCursor();
    int getLimit();
    Direction getDirection();
    SliceIndicator<T> reverse();
    SliceIndicator<T> next(T nextCursor);
}
