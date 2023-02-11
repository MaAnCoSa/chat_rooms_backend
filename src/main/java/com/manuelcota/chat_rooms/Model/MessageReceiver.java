package com.manuelcota.chat_rooms.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageReceiver {
    private String roomID;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
}
