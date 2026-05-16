package com.Sadetechno.comment_module.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reels_comments") // Specify the MongoDB collection name
public class CommentReels {

    @Id
    private String id; // Use String for MongoDB ObjectId

    private Long reelsId;
    private Long userId;
    private String name;
    private Long repliedToUserId;

    private String textContent;

    @CreatedDate // Automatically store the creation date
    private LocalDateTime createdAt;

    @DBRef     // Use DBRef for referencing another document
    @EqualsAndHashCode.Exclude
    private CommentReels parentComment;

    private String parentIdName;

    private String imagePath;

    @DBRef      // Use DBRef for replies if they are stored in a separate collection
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Set<CommentReels> replies = new HashSet<>();
}
