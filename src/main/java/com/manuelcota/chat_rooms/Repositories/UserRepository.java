package com.manuelcota.chat_rooms.Repositories;

import com.manuelcota.chat_rooms.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Interface to manage the user repository.
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // This method finds a given user by its email (its main ID).
    Optional<User> findByUsername(String username);
    // This method confirms if a user exists with a given email (its main ID).
    boolean existsByUsername(String username);

}