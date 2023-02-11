package com.manuelcota.chat_rooms.Repositories;

import com.manuelcota.chat_rooms.Model.Conversation;
import com.manuelcota.chat_rooms.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
// Interface to manage the room repository.
public interface MessageRepository extends JpaRepository<Message, String> {
    // This method finds a message by its ID.
    Optional<Conversation> findById(UUID id);
    // This method confirms if a message exists with a given ID.
    boolean existsById(UUID id);
    boolean existsBySenderName(String senderName);
    boolean existsByReceiverName(String receiverName);
}