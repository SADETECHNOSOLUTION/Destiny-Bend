package com.sadetech.websocket_messaging.Service;

import com.sadetech.websocket_messaging.Model.Conversation;
import com.sadetech.websocket_messaging.Model.Message;
import com.sadetech.websocket_messaging.Repository.ConversationRepository;
import com.sadetech.websocket_messaging.Repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public void saveMessage(Message message) {
        Optional<Conversation> conversationOptional = findConversationByParticipants(message.getSenderId(), message.getRecipientId());

        if (conversationOptional.isPresent()) {
            // Existing conversation found
            Conversation conversation = conversationOptional.get();
            logger.info("Conversation id found, {} and {}",conversation.getParticipantOneId(),conversation.getParticipantTwoId());
            message.setConversation(conversation);  // Associate message with the existing conversation
            messageRepository.save(message);  // Save the message
            logger.info("Message saved {}",message);
        } else {
            // No conversation found, create a new one
            Conversation newConversation = new Conversation();
            newConversation.setParticipantOneId(message.getSenderId());
            logger.info("New sender id for new conversation {}",message.getSenderId());
            newConversation.setParticipantTwoId(message.getRecipientId());
            logger.info("New recipient id for new conversation {}",message.getRecipientId());

            // Save the new conversation first
            conversationRepository.save(newConversation);  // Save the conversation to get an ID

            // Now associate the message with the new conversation
            message.setConversation(newConversation);
            logger.info("New message for conversation is {}",message);
            messageRepository.save(message);  // Save the message with a valid conversation reference
        }
    }

    // Utility to find a conversation by participants
    private Optional<Conversation> findConversationByParticipants(Long senderId, Long recipientId) {
        List<Conversation> allConversations = conversationRepository.findAll();
        for (Conversation conversation : allConversations) {
            if ((conversation.getParticipantOneId().equals(senderId) && conversation.getParticipantTwoId().equals(recipientId)) ||
                    (conversation.getParticipantOneId().equals(recipientId) && conversation.getParticipantTwoId().equals(senderId))) {
                return Optional.of(conversation);
            }
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Conversation> getConversationWithMessages(Long participantOneId, Long participantTwoId) {
        Optional<Conversation> conversation = conversationRepository.findByParticipants(participantOneId, participantTwoId);

        conversation.ifPresent(conv -> {
            List<Message> messages = messageRepository.findByConversationId(conv.getId());
            conv.setMessages(messages); // Manually set messages
        });

        return conversation;
    }

    @Transactional(readOnly = true)
    public List<Conversation> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findAll();
        if (conversations.isEmpty()) {
            throw new IllegalArgumentException("No conversations found");
        }

        // Fetch and set messages for each conversation
        conversations.forEach(conversation -> {
            List<Message> messages = messageRepository.findByConversationId(conversation.getId());
            conversation.setMessages(messages);
        });

        return conversations;
    }

    @Transactional
    public void deleteMessageForSelf(String id, Long userId) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with ID: " + id));

        if (message.getSenderId().equals(userId)) {
            message.setDeletedBySender(true);
        } else if (message.getRecipientId().equals(userId)) {
            message.setDeletedByRecipient(true);
        } else {
            throw new IllegalArgumentException("User is not authorized to delete this message.");
        }

        messageRepository.save(message);

        if (message.isDeletedBySender() && message.isDeletedByRecipient()) {
            messageRepository.delete(message);
        }
    }

    @Transactional
    public void deleteMessageForEveryone(String id, Long userId) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with ID: " + id));

        logger.info("Attempting to delete message with id: {}", id);

        if (message.getSenderId().equals(userId)) {
            logger.info("User {} is authorized to delete message {}", userId, id);
            messageRepository.delete(message);
            logger.info("Message {} deleted successfully", id);
        } else {
            logger.warn("User {} is not authorized to delete message {}", userId, id);
            throw new IllegalArgumentException("Only the sender can delete the message for everyone.");
        }
    }
}
