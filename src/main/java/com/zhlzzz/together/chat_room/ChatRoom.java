package com.zhlzzz.together.chat_room;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "chat_room_user")
    private Set<Long> userIds = new HashSet<Long>();

    @Getter @Setter
    @Column
    private String name;

    @Getter @Setter
    @Column
    private LocalDateTime createTime;

}
