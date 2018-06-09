package com.zhlzzz.together.websocket;

import lombok.*;

@Data
@ToString
public class ChatMessage {


    enum Type {
        people, speak
    }

    private Type type;

    private Long uid;

    private String word;

    public ChatMessage(Type type, Long uid, String word) {
        this.type = type;
        this.uid = uid;
        this.word = word;
    }

    public Type getType() {
        return type;
    }

    public Long getUid() { return uid; }

    public String getWord() { return word; }
}
