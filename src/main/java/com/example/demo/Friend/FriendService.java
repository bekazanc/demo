package com.example.demo.Friend;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.Users.UserRepository;
import com.example.demo.Users.User;

@Service
public class FriendService {
    private final UserRepository userRepository;

    public FriendService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void sendFriendRequest(String fromEmail, String toEmail) {
        User fromUser = userRepository.findByEmail(fromEmail)
                .orElseThrow(() -> new RuntimeException("Sender user not found: " + fromEmail));
        User toUser = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new RuntimeException("Recipient user not found: " + toEmail));

        if (toUser.getFriendRequests().contains(fromEmail)) {
            throw new RuntimeException("Friend request already sent from " + fromEmail + " to " + toEmail);
        }

        if (fromUser.getFriends().contains(toEmail)) {
            throw new RuntimeException("The user " + toEmail + " is already in your friends list.");
        }

        toUser.getFriendRequests().add(fromEmail);
        userRepository.save(toUser);
    }


    public void acceptFriendRequest(String userEmail, String fromEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
        User fromUser = userRepository.findByEmail(fromEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + fromEmail));

        if (!user.getFriendRequests().contains(fromEmail)) {
            throw new RuntimeException("No friend request from " + fromEmail + " to " + userEmail);
        }

        if (user.getFriends().contains(fromEmail)) {
            throw new RuntimeException("The user " + fromEmail + " is already in your friends list.");
        }

        user.getFriendRequests().remove(fromEmail);
        user.getFriends().add(fromEmail);
        fromUser.getFriends().add(userEmail);

        userRepository.save(user);
        userRepository.save(fromUser);
    }

    public List<String> getFriends(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
        return user.getFriends();
    }
}

