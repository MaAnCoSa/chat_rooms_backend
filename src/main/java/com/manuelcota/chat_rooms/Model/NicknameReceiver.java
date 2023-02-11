package com.manuelcota.chat_rooms.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NicknameReceiver {
    @NotBlank
    private String username;
    private UUID id;
    private String nickname;
    private boolean byCookie;
}
