package com.manuelcota.chat_rooms.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    @Id
    private UUID id;
    private UUID roomID;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
}
