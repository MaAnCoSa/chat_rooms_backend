package com.manuelcota.chat_rooms.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NicknameStatus {
    private String nickname;
    private String status;
    private int notif;
}
