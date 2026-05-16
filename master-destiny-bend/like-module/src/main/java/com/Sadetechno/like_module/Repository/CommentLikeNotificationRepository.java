package com.Sadetechno.like_module.Repository;

import com.Sadetechno.like_module.model.CommentLikeNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeNotificationRepository extends MongoRepository<CommentLikeNotification,String> {
    void deleteByIdAndType(String id, String type);
}
