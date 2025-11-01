package com.example.demo.Users;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);



    boolean existsByEmail(String email);

    // Check if two users are friends
    default boolean areFriends(String email1, String email2) {
        // Find the two users by their emails
        User user1 = findByEmail(email1).orElse(null);
        User user2 = findByEmail(email2).orElse(null);

        // If either user is not found, they cannot be friends
        if (user1 == null || user2 == null) {
            return false;
        }

        // Check if they are friends
        return user1.getFriends().contains(email2) && user2.getFriends().contains(email1);
    }
}
