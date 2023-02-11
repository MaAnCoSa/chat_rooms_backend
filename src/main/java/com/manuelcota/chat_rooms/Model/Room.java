package com.manuelcota.chat_rooms.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    private UUID id;
    private List<String> users;
    private List<String> statuses;
    private List<String> conversations;

    @ManyToMany
    private List<User> userList;

}
