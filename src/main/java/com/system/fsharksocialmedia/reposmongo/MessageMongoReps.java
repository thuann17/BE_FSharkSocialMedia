package com.system.fsharksocialmedia.reposmongo;


import com.system.fsharksocialmedia.documents.MessageMongo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageMongoReps extends MongoRepository<MessageMongo, ObjectId> {
    List<MessageMongo> findAllBySenderAndRecipient(String sender, String recipient);
}
