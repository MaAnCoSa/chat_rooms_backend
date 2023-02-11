package com.manuelcota.chat_rooms.Repositories;

import com.manuelcota.chat_rooms.Model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
// Interface to manage the room repository.
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    // This method finds a conversation by its ID.
    Optional<Conversation> findById(UUID id);
    Optional<Conversation> findByUser1AndUser2AndRoomId(String user1, String user2, UUID roomId);

    // This method confirms if a conversation exists with a given ID.
    boolean existsById(UUID id);
    boolean existsByUser1AndUser2AndRoomId(String user1, String user2, UUID roomId);
}