package com.manuelcota.chat_rooms.Repositories;

import com.manuelcota.chat_rooms.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
// Interface to manage the room repository.
public interface RoomRepository extends JpaRepository<Room, String> {
    // This method finds a given room by ID.
    Optional<Room> findById(UUID id);
    // This method confirms if a room exists with a given ID.
    boolean existsById(UUID id);
}