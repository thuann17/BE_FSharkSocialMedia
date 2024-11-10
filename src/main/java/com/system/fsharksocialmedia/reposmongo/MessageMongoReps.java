package com.system.fsharksocialmedia.reposmongo;


import com.system.fsharksocialmedia.documents.MessageMongo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageMongoReps extends MongoRepository<MessageMongo, ObjectId> {
    @Query("{ $or: [ { 'sender': ?0, 'reciver': ?1 }, { 'sender': ?1, 'reciver': ?0 } ] }")
    List<MessageMongo> findMessagesBetweenUsers(String user1, String user2);
}

