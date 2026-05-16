package com.sadetech.websocket_messaging.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "message")
public class Message {
    @Id
    private String id;

    private Long senderId;
    private Long recipientId;

    private Long mediaId;

    private String content;

    private boolean isRead = false;

    @CreatedDate
    private LocalDateTime sentAt;

    @DBRef
    @JsonIgnore
    private Conversation conversation;

    private boolean deletedBySender = false;
    private boolean deletedByRecipient = false;

}
