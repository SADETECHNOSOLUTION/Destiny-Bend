package com.sadetech.websocket_messaging.Repository;

import com.sadetech.websocket_messaging.Model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends MongoRepository<Message,String > {
    List<Message> findByConversationId(String conversationId);
}
