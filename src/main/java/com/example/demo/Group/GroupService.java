package com.example.demo.Group;

import com.example.demo.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private UserRepository userRepository;

    public Group createGroup(String name, List<String> members) {
        Group group = new Group();
        group.setName(name);
        group.setMembers(members);
        return groupRepository.save(group);
    }

    public Group addMember(String groupId, String memberEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getMembers().contains(memberEmail)) {
            throw new IllegalArgumentException("Member already exists in the group");
        }

        group.getMembers().add(memberEmail);
        return groupRepository.save(group);
    }

    public void sendGroupMessage(String groupId, String senderEmail, String content) {
        // Validate group existence
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found: " + groupId));

        // Validate sender existence
        if (!userRepository.existsByEmail(senderEmail)) {
            throw new RuntimeException("Sender does not exist: " + senderEmail);
        }

        // Save group message
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setGroupId(groupId);
        groupMessage.setSenderEmail(senderEmail);
        groupMessage.setContent(content);
        groupMessageRepository.save(groupMessage);
    }

    public List<GroupMessage> getGroupMessages(String groupId) {
        return groupMessageRepository.findByGroupId(groupId);
    }

    public List<String> getGroupMembers(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return group.getMembers();
    }

    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }
}
