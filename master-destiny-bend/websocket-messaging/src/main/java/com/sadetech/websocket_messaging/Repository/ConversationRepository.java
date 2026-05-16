package com.sadetech.websocket_messaging.Repository;

import com.sadetech.websocket_messaging.Model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation,String > {


    @Query("{ '$or': [ " +
            "{ 'participantOneId': ?0, 'participantTwoId': ?1 }, " +
            "{ 'participantOneId': ?1, 'participantTwoId': ?0 } " +
            "] }")
    Optional<Conversation> findByParticipants(Long senderId, Long recipientId);

}
