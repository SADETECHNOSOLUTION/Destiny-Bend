package com.sadetech.websocket_messaging.Controller;

import com.sadetech.websocket_messaging.Model.Conversation;
import com.sadetech.websocket_messaging.Model.Message;
import com.sadetech.websocket_messaging.Service.ConversationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/web-socket")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @PostMapping("/chat")
    public ResponseEntity<?> saveMessage(@RequestBody Message message){
        try {
            conversationService.saveMessage(message);
            logger.info("Message Controller save message {}",message);
           return ResponseEntity.status(HttpStatus.CREATED).body("Message posted...");
        } catch (Exception e) {
            logger.info("Has issue in posting message , {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error in posting message");
        }
    }

    @GetMapping("/conversation")
    public ResponseEntity<Conversation> getConversationByParticipants(
            @RequestParam Long participantOneId, @RequestParam Long participantTwoId) {

        Optional<Conversation> conversation = conversationService.getConversationWithMessages(participantOneId, participantTwoId);

        return conversation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().build());
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<Conversation>> getAllConversations() {
        try {
            List<Conversation> conversations = conversationService.getAllConversations();
            return ResponseEntity.ok(conversations);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete-for-everyone/{id}")
    public ResponseEntity<String> deleteMessageForEveryone(
            @PathVariable String id,
            @RequestParam Long userId) {

        try {
            conversationService.deleteMessageForEveryone(id, userId);
            return ResponseEntity.ok("Message deleted for everyone");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-for-self/{id}")
    public ResponseEntity<String> deleteMessageForSelf(
            @PathVariable String id,
            @RequestParam Long userId) {

        try {
            conversationService.deleteMessageForSelf(id, userId);
            return ResponseEntity.ok("Message deleted for yourself");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
