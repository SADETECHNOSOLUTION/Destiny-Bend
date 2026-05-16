package com.Sadetechno.like_module.Repository;

import com.Sadetechno.like_module.model.PostNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostNotificationRepository extends MongoRepository<PostNotification,String> {
    void deleteByIdAndType(String id, String type);
}
