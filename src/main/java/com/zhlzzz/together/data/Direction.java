package com.zhlzzz.together.data;

public enum Direction {

    FORWARD, BACKWARD;

    public boolean isForward() {
        return this.equals(FORWARD);
    }

    public boolean isBackward() {
        return this.equals(BACKWARD);
    }

    public Direction reverse() {
        return this.equals(FORWARD) ? BACKWARD : FORWARD;
    }
}
