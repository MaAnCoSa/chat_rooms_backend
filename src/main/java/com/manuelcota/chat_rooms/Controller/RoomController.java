package com.manuelcota.chat_rooms.Controller;

import com.manuelcota.chat_rooms.Model.Conversation;
import com.manuelcota.chat_rooms.Model.NicknameReceiver;
import com.manuelcota.chat_rooms.Model.Room;
import com.manuelcota.chat_rooms.Repositories.RoomRepository;
import com.manuelcota.chat_rooms.Service.RoomService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepo;

    @PostMapping("/create_room")
    public ResponseEntity<?> createRoom(@RequestBody @Valid NicknameReceiver nicknameReceiver, HttpServletResponse response) {
        System.out.println("Room created");
        String username = nicknameReceiver.getUsername();
        String nickname = nicknameReceiver.getNickname();
        return roomService.createRoom(username, nickname, response);
    }

    @PostMapping("/join_room")
    public ResponseEntity<?> joinRoom(@RequestBody @Valid NicknameReceiver nicknameReceiver, HttpServletResponse response) {
        System.out.println("Joined room");
        String username = nicknameReceiver.getUsername();
        String nickname = nicknameReceiver.getNickname();
        boolean byCookie = nicknameReceiver.isByCookie();
        UUID id = nicknameReceiver.getId();
        return roomService.joinRoom(username, id, nickname, byCookie, response);
    }

    @PostMapping("/open_conversation")
    public ResponseEntity<?> openConversation(@RequestBody Conversation conversation) {
        UUID id = conversation.getRoomId();
        System.out.println("opened conversation");
        System.out.println("ROOM ID SENT: " + id);
        System.out.println(id);
        System.out.println(conversation.toString());
        String user1 = conversation.getUser1();
        Room room = roomRepo.findById(id).get();
        Map<String, String> users = new HashMap<>();
        room.getUsers().forEach(i -> {
            String[] pair = i.split("=");
            users.put(pair[1], pair[0]);
        });
        String user2 = users.get(conversation.getUser2());
        return roomService.openConversation(id, user1, user2);
    }
}
