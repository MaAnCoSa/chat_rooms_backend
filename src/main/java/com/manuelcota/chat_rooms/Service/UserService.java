package com.manuelcota.chat_rooms.Service;

import com.manuelcota.chat_rooms.Model.User;
import com.manuelcota.chat_rooms.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;


@Service
public class UserService {

    // Instance of the user repository.
    @Autowired
    UserRepository userRepo;

    // Instance of the password encoder.
    @Autowired
    PasswordEncoder encoder;

    public ResponseEntity<?> registerUser(User user) {
        // If the user already exists...
        if (userRepo.existsByUsername(user.getUsername())
                || user.getUsername() == null  // ...or if no email was given...
                || user.getPassword() == null) {    // ...or if no password was given...
            // ... a BAD REQUEST response is returned.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            // If the user does not exist...
        } else {
            // ...the password is encoded.
            user.setPassword(encoder.encode(user.getPassword()));
            // The UUID is generated.
            user.setToken(UUID.randomUUID());
            // The user is saved into the user repository.
            userRepo.save(user);
            return new ResponseEntity<>(Map.of("token", user.getToken()), HttpStatus.OK);
            //return "{\"token\":" + user.getToken() + "}";

        }
    }

    public ResponseEntity<?> loginUser(User user) {
        // If the user already exists...
        System.out.println(user.getUsername());
        if (!userRepo.existsByUsername(user.getUsername())) {    // ...or if no password was given...
            // ... a BAD REQUEST response is returned.
            System.out.println("not found");
            throw new wrongParam("No username found!");
            // If the user does not exist...
        } else if (user.getUsername() == null) {
            System.out.println("no username given");
            throw new wrongParam("No username given!");
        } else if (user.getPassword() == null) {
            System.out.println("no password given");
            throw new wrongParam("No password given!");
        } else {
            User savedUser = userRepo.findByUsername(user.getUsername()).get();
            if (encoder.matches(user.getPassword(), savedUser.getPassword())) {
                return new ResponseEntity<>(Map.of("token", savedUser.getToken()), HttpStatus.OK);
                //return "{\"token\":" + savedUser.getToken() + "}";
            } else {
                System.out.println("wrong password");
                System.out.println("given: " + savedUser.getPassword());
                System.out.println("had:   " + encoder.encode(user.getPassword()));
                throw new wrongParam("Wrong password!");
            }
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
}
