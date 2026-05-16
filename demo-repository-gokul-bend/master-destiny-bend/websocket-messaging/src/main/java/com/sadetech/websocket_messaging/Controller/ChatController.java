package com.sadetech.websocket_messaging.Controller;

import com.sadetech.websocket_messaging.Model.Message;
import com.sadetech.websocket_messaging.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class  ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // WebSocket endpoint for receiving messages from clients
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message receiveMessage(Message message) {
        // Save message to the conversation in the database
        chatService.saveMessage(message);
        return message;
    }

    // Programmatically broadcast message to a specific conversation topic
    public void broadcastToConversation(Long conversationId, Message message) {
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, message);
    }

}
