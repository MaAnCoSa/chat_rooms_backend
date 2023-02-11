package com.manuelcota.chat_rooms.Controller;

import com.manuelcota.chat_rooms.Model.*;
import com.manuelcota.chat_rooms.Repositories.ConversationRepository;
import com.manuelcota.chat_rooms.Repositories.MessageRepository;
import com.manuelcota.chat_rooms.Repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private ConversationRepository converRepo;

    @Autowired
    private MessageRepository messageRepo;

    @MessageMapping("/room/{id}/{username}") //  /app/room/id/username
    public void userUpdate(@Payload NicknameStatus[] nicknames, @DestinationVariable String id, @DestinationVariable String username) {
        simpMessagingTemplate.convertAndSend("/user_updates/" + id, nicknames); // /user_updates/id
        System.out.println(id);
        System.out.println(username);
        System.out.println("UPDATE RECEIVED: ");
        System.out.println(nicknames.toString());
    }

    @MessageMapping("/room/{id}/{username}/update_statuses") //  /app/room/id/username
    public void statusUpdate(@Payload NicknameStatus[] nicknames, @DestinationVariable String id, @DestinationVariable String username) {
        UUID roomId = UUID.fromString(id);
        Room room = roomRepo.findById(roomId).get();

        Map<String, String> users = new HashMap<>();
        room.getUsers().forEach(i -> {
            String[] pair = i.split("=");
            users.put(pair[1], pair[0]);
        });

        List<String> conversIds = room.getConversations();
        for (int j = 0; j < nicknames.length; j++) {
            String otherUser = users.get(nicknames[j].getNickname());
            for (int i = 0; i < conversIds.size(); i++) {
                Conversation conver = converRepo.findById(UUID.fromString(conversIds.get(i))).get();
                if (conver.getUser1().equals(username) && conver.getUser2().equals(otherUser)) {
                    conver.setNotif1(Integer.toString(nicknames[j].getNotif()));
                } else if (conver.getUser2().equals(username) && conver.getUser1().equals(otherUser)) {
                    conver.setNotif2(Integer.toString(nicknames[j].getNotif()));
                }
            }
        }


        List<String> newStats = new ArrayList<>();
        for (int i = 0; i < nicknames.length; i++) {
            String stat = nicknames[i].getNickname() + "=" + nicknames[i].getStatus();
            newStats.add(stat);
        }

        room.setStatuses(newStats);
        roomRepo.save(room);

        simpMessagingTemplate.convertAndSend("/user_updates/" + id, nicknames); // /user_updates/id
        System.out.println(username);
        System.out.println("STATUS UPDATE RECEIVED: ");
        System.out.println(nicknames);
    }

    @MessageMapping("/room/{id}/{username}/{converId}/update_statuses") //  /app/room/id/username
    public void notifUpdate(@Payload NicknameStatus[] nicknames, @DestinationVariable String id, @DestinationVariable String username, @DestinationVariable String converId) {
        UUID roomId = UUID.fromString(id);
        Room room = roomRepo.findById(roomId).get();

        Conversation conver = converRepo.findById(UUID.fromString(converId)).get();
        if (conver.getUser1().equals(username)) {
            conver.setNotif1("0");
        } else if (conver.getUser2().equals(username)) {
            conver.setNotif2("0");
        }
        converRepo.save(conver);

        List<String> newStats = new ArrayList<>();
        for (int i = 0; i < nicknames.length; i++) {
            String stat = nicknames[i].getNickname() + "=" + nicknames[i].getStatus();
            newStats.add(stat);
        }

        room.setStatuses(newStats);
        roomRepo.save(room);

        simpMessagingTemplate.convertAndSend("/user_updates/" + id, nicknames); // /user_updates/id
        System.out.println(username);
        System.out.println("STATUS UPDATE RECEIVED: ");
        System.out.println(nicknames);
    }

    @MessageMapping("/message/{id}/{username}")
    public void receiveMessage(@Payload MessageReceiver inMessage, @DestinationVariable String id, @DestinationVariable String username) {
        Message message = new Message();
        message.setId(UUID.randomUUID());
        Room room = roomRepo.findById(UUID.fromString(id)).get();
        room.setId(UUID.fromString(id));
        Map<String, String> users = new HashMap<>();
        Map<String, String> nicks = new HashMap<>();
        room.getUsers().forEach(i -> {
            String[] pair = i.split("=");
            users.put(pair[1], pair[0]);
            nicks.put(pair[0], pair[1]);
        });
        String user1Nick = nicks.get(inMessage.getSenderName());
        String receiver = users.get(inMessage.getReceiverName());
        System.out.println(receiver);

        message.setMessage(inMessage.getMessage());
        message.setReceiverName(receiver);
        message.setSenderName(inMessage.getSenderName());
        message.setDate(inMessage.getDate());
        message.setRoomID(UUID.fromString(id));
        message.setReceiverName(receiver);
        messageRepo.save(message);


        boolean exists12 = converRepo.existsByUser1AndUser2AndRoomId(message.getSenderName(), receiver, room.getId());
        boolean exists21 = converRepo.existsByUser1AndUser2AndRoomId(receiver, message.getSenderName(), room.getId());



        if (exists12) {
            Conversation conver = converRepo.findByUser1AndUser2AndRoomId(message.getSenderName(), receiver, room.getId()).get();
            List<Message> newList = conver.getMessages();
            newList.add(message);
            conver.setMessages(newList);
            int not = Integer.parseInt(conver.getNotif1()) + 1;
            conver.setNotif1(Integer.toString(not));
            converRepo.save(conver);

            conver.setUser1(user1Nick);
            conver.setUser2(inMessage.getReceiverName());
            simpMessagingTemplate.convertAndSend("/user/" + id + "/" + message.getReceiverName(), conver);
        }
        if (exists21) {
            Conversation conver = converRepo.findByUser1AndUser2AndRoomId(receiver, message.getSenderName(), room.getId()).get();
            List<Message> newList = conver.getMessages();
            newList.add(message);
            conver.setMessages(newList);
            int not = Integer.parseInt(conver.getNotif1()) + 1;
            conver.setNotif1(Integer.toString(not));
            converRepo.save(conver);

            conver.setUser1(user1Nick);
            conver.setUser2(inMessage.getReceiverName());
            simpMessagingTemplate.convertAndSend("/user/" + id + "/" + message.getReceiverName(), conver);
        }

        System.out.println("Message RECEIVED: ");
        System.out.println(message);
    }

}
