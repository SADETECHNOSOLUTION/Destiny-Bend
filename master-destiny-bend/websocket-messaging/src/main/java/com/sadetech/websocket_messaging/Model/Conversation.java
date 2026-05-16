package com.sadetech.websocket_messaging.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "conversation")
public class Conversation {
    @Id
    private String id;

    private Long participantOneId;
    private Long participantTwoId;

    @DBRef
    private List<Message> messages = new ArrayList<>(); // Initialize the list
}

