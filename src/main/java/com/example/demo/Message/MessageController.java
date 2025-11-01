package com.example.demo.Message;

import com.example.demo.Message.Message;
import com.example.demo.Message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        try {
            messageService.sendMessage(message.getSenderEmail(), message.getReceiverEmail(), message.getContent());
            return ResponseEntity.ok("Message sent");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getConversation(@RequestParam String userEmail1, @RequestParam String userEmail2) {
        try {
            List<Message> conversation = messageService.getConversation(userEmail1, userEmail2);
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
