package com.example.demo.Message;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    @Query("{$or: [ { 'senderEmail': ?0, 'receiverEmail': ?1 }, { 'senderEmail': ?1, 'receiverEmail': ?0 } ]}")
    List<Message> findByConversation(String userEmail1, String userEmail2);

}

