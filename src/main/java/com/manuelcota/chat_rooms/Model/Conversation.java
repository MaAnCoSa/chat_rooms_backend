package com.manuelcota.chat_rooms.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="conversation")
public class Conversation {
    @Id
    private UUID id;
    private UUID roomId;
    private String user1;
    private String user2;

    private String notif1;
    private String notif2;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Message> messages;
}
