package com.zhlzzz.together.chat_room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom addChatRoom(String name, Set<Long> userIds) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setUserIds(userIds);
        chatRoom.setCreateTime(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> getChatRoom(Long id) {
        return chatRoomRepository.getById(id);
    }
}
