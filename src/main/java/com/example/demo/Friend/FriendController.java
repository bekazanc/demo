package com.example.demo.Friend;

import com.example.demo.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    @Autowired
    private UserRepository userRepository;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest request) {
        // Check if both sender and receiver exist in the system
        if (!userRepository.existsByEmail(request.getSenderEmail())) {
            return ResponseEntity.badRequest().body("Sender email is not registered in the system.");
        }

        if (!userRepository.existsByEmail(request.getReceiverEmail())) {
            return ResponseEntity.badRequest().body("Receiver email is not registered in the system.");
        }

        try {
            friendService.sendFriendRequest(request.getSenderEmail(), request.getReceiverEmail());
            return ResponseEntity.ok("Friend request sent");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }



    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");
        String fromEmail = request.get("fromEmail");

        if (userEmail == null || fromEmail == null) {
            return ResponseEntity.badRequest().body("Both userEmail and fromEmail must be provided.");
        }

        try {
            friendService.acceptFriendRequest(userEmail, fromEmail);
            return ResponseEntity.ok("Friend request from " + fromEmail + " accepted by " + userEmail);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/getfriends")
    public ResponseEntity<?> getFriends(@RequestParam String userEmail) {
        try {
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.badRequest().body("Email must be provided and cannot be empty.");
            }

            List<String> friends = friendService.getFriends(userEmail);

            if (friends.isEmpty()) {
                return ResponseEntity.ok("No friends found for the user with email: " + userEmail);
            }

            return ResponseEntity.ok(friends);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("User not found with email: " + userEmail);
        }
    }
}
