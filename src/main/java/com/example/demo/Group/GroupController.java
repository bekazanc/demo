package com.example.demo.Group;

import com.example.demo.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest) {
        for (String memberEmail : groupRequest.getMembers()) {
            if (!userRepository.existsByEmail(memberEmail)) {
                return ResponseEntity.badRequest().body("Member with email " + memberEmail + " does not exist.");
            }
        }
        return ResponseEntity.ok(groupService.createGroup(groupRequest.getName(), groupRequest.getMembers()));
    }

    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<String> addMember(@PathVariable String groupId, @RequestParam String memberEmail) {

        if (!userRepository.existsByEmail(memberEmail)) {
            return ResponseEntity.badRequest().body("User with email " + memberEmail + " does not exist.");
        }


        Group group = groupService.getGroupById(groupId); // groupId'ye göre grubu getir
        if (group.getMembers().contains(memberEmail)) {
            return ResponseEntity.badRequest().body("User with email " + memberEmail + " is already a member of this group.");
        }


        groupService.addMember(groupId, memberEmail);


        return ResponseEntity.ok("User " + memberEmail + " added to the group successfully.");
    }


    @PostMapping("/{groupId}/send")
    public ResponseEntity<?> sendGroupMessage(@PathVariable String groupId, @RequestBody GroupMessage request) {
        try {
            Group group = groupService.getGroupById(groupId);

            if (!group.getMembers().contains(request.getSenderEmail())) {
                return ResponseEntity.badRequest().body("Error: Sender is not a member of the group.");
            }

            groupService.sendGroupMessage(groupId, request.getSenderEmail(), request.getContent());
            return ResponseEntity.ok("Message sent to group");
        } catch (Exception e) {

            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }



    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<GroupMessage>> getGroupMessages(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroupMessages(groupId));
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<String>> getGroupMembers(@PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroupMembers(groupId));
    }
}
