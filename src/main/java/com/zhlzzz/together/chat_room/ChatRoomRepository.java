package com.zhlzzz.together.chat_room;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ChatRoomRepository extends Repository<ChatRoom, Long>{

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> getById(Long id);

}
