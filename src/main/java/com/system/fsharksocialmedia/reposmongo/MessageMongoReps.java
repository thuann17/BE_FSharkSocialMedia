package com.system.fsharksocialmedia.reposmongo;

import com.system.fsharksocialmedia.documents.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageMongoReps extends MongoRepository<MessageModel, String> {
    @Query("{ $or: [ { 'sender': ?0, 'receiver': ?1 }, { 'sender': ?1, 'receiver': ?0 } ] }")
    List<MessageModel> findMessagesBetweenUsers(String user1, String user2);
    // Xóa tin nhắn của một người dùng (user1) gửi cho (hoặc nhận từ) user2
    void deleteBySenderAndReceiver(String sender, String receiver);
}

