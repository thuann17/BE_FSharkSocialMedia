package com.system.fsharksocialmedia.reposmongo;

import com.system.fsharksocialmedia.documents.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageMongoReps extends MongoRepository<MessageModel, String> {
    List<MessageModel> findBySenderAndReceiverOrReceiverAndSender(String sender1, String receiver1, String sender2, String receiver2);
}

