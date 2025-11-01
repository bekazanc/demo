package com.example.demo.Message;

import com.example.demo.Users.User;
import com.example.demo.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessage(String senderEmail, String receiverEmail, String content) {

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderEmail));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + receiverEmail));

        if (!sender.getFriends().contains(receiverEmail)) {
            throw new RuntimeException("You can only send messages to your friends.");
        }


        Message message = new Message();
        message.setSenderEmail(senderEmail);
        message.setReceiverEmail(receiverEmail);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
    }

    public List<Message> getConversation(String userEmail1, String userEmail2) {

        User user1 = userRepository.findByEmail(userEmail1)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail1));
        User user2 = userRepository.findByEmail(userEmail2)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail2));

        if (!user1.getFriends().contains(userEmail2)) {
            throw new RuntimeException("You can only view conversations with your friends.");
        }


        return messageRepository.findByConversation(userEmail1, userEmail2);
    }
}
