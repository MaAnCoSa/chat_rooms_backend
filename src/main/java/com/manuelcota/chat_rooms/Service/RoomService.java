package com.manuelcota.chat_rooms.Service;

import com.manuelcota.chat_rooms.Model.Conversation;
import com.manuelcota.chat_rooms.Model.Room;
import com.manuelcota.chat_rooms.Repositories.ConversationRepository;
import com.manuelcota.chat_rooms.Repositories.RoomRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@Service
public class RoomService {

    // Instance of the user repository.
    @Autowired
    RoomRepository roomRepo;

    @Autowired
    ConversationRepository converRepo;

    public ResponseEntity<?> createRoom(String username, String nickname, HttpServletResponse response) {
        Room newRoom = new Room();
        newRoom.setUsers(new ArrayList<>());
        newRoom.setStatuses(new ArrayList<>());
        newRoom.setId(UUID.randomUUID());
        newRoom.setConversations(new ArrayList<>());
        String newUser = username + "=" + nickname;
        newRoom.getUsers().add(newUser);
        String newNickname = nickname + "=ONLINE";
        newRoom.getStatuses().add(newNickname);
        roomRepo.save(newRoom);

        return new ResponseEntity<>(Map.of("roomId", newRoom.getId()), HttpStatus.OK);
    }

    public ResponseEntity<?> joinRoom(String username, UUID id, String nickname, boolean byCookie, HttpServletResponse response) {
        if (roomRepo.existsById(id)) {
            Room room = roomRepo.findById(id).get();
            Map<String, String> users = new HashMap<>();
            room.getUsers().forEach(i -> {
                String[] pair = i.split("=");
                users.put(pair[0], pair[1]);
            });
            Collection<String> nickList = users.values();

            Map<String, String> statuses = new HashMap<>();
            if (room.getStatuses().size() != 0) {
                room.getStatuses().forEach(i -> {
                    String[] pair = i.split("=");
                    statuses.put(pair[0], pair[1]);
                });
            }
            String oldNickname = users.get(username);
            statuses.replace(oldNickname, "ONLINE");
            room.getStatuses().add(oldNickname + "=ONLINE");
            Collection<String> statList = statuses.values();


            if (byCookie && users.containsKey(username)) {
                return new ResponseEntity<>(Map.of("nickname", oldNickname, "users", users.keySet(), "nicknames", nickList, "statuses", statList), HttpStatus.OK);
            } else if (byCookie && !users.containsKey(username)) {
                return new ResponseEntity<>(Map.of("customException", "User not allowed into cookie's room ID."), HttpStatus.OK);
            }

            if (!users.containsKey(username)) {
                String newUser = username + "=" + nickname;
                String newNickname = nickname + "=ONLINE";
                room.getUsers().add(newUser);
                room.getStatuses().add(newNickname);
                users.put(username, nickname);
                statuses.put(nickname, "ONLINE");
                roomRepo.save(room);

                return new ResponseEntity<>(Map.of("nickname", nickname, "users", users.keySet(), "nicknames", nickList, "statuses", statList), HttpStatus.OK);
            } else if (nickname == "") {
                return new ResponseEntity<>(Map.of("nickname", oldNickname, "users", users.keySet(), "nicknames", nickList, "statuses", statList), HttpStatus.OK);
            } else {
                String updatedUser = username + "=" + nickname;
                String newNickname = nickname + "=ONLINE";
                room.getUsers().remove(username + "=" + oldNickname);
                room.getUsers().add(updatedUser);
                room.getStatuses().add(newNickname);
                users.put(username, nickname);
                statuses.put(nickname, "ONLINE");
                roomRepo.save(room);

                return new ResponseEntity<>(Map.of("nickname", nickname, "users", users.keySet(), "nicknames", nickList, "statuses", statList), HttpStatus.OK);

            }
        } else if (byCookie) {
            return new ResponseEntity<>(Map.of("customException", "Cookie's room ID not existent anymore."), HttpStatus.OK);
        } else {
            throw new wrongParam("Invalid room ID.");
        }
    }

    // Custom response for when there is not only 1 parameter.
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    static
    class wrongParam extends RuntimeException {
        public wrongParam(String cause) {
            super(cause);
        }
    }

    public ResponseEntity<?> openConversation(UUID id, String user1, String user2) {
        boolean exists12 = converRepo.existsByUser1AndUser2AndRoomId(user1, user2, id);
        boolean exists21 = converRepo.existsByUser1AndUser2AndRoomId(user2, user1, id);
        Room room = roomRepo.findById(id).get();
        if (exists12) {
            Conversation conver = converRepo.findByUser1AndUser2AndRoomId(user1, user2, id).get();
            return new ResponseEntity<>(conver, HttpStatus.OK);
        } else if (exists21) {
            Conversation conver = converRepo.findByUser1AndUser2AndRoomId(user2, user1, id).get();
            return new ResponseEntity<>(conver, HttpStatus.OK);
        } else {
            Conversation newConver = new Conversation();
            newConver.setId(UUID.randomUUID());
            newConver.setUser1(user1);
            newConver.setUser2(user2);
            newConver.setNotif1(Integer.toString(0));
            newConver.setNotif2(Integer.toString(0));
            newConver.setMessages(new ArrayList<>());
            newConver.setRoomId(id);
            converRepo.save(newConver);
            room.getConversations().add(newConver.getId().toString());
            roomRepo.save(room);
            return new ResponseEntity<>(newConver, HttpStatus.OK);
        }
    }
}
