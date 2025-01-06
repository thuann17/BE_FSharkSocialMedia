package com.system.fsharksocialmedia.reposmongo;

import com.system.fsharksocialmedia.documents.MessageModel;
import com.system.fsharksocialmedia.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageMongoReps extends MongoRepository<MessageModel, String> {
    @Query("{ $and: [ " +
            "{ $or: [ { 'sender': ?0 }, { 'receiver': ?0 } ] }, " +
            "{ $or: [ { 'sender': ?1 }, { 'receiver': ?1 } ] }, " +
            "{ 'isDeletedBySender': false, 'isDeletedByReceiver': false } " +
            "] }")
    List<MessageModel> findMessagesForUsers(String user1, String user2);
}

