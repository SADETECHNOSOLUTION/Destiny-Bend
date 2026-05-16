package com.Sadetechno.like_module.Repository;

import com.Sadetechno.like_module.model.ReelsNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReelsNotificationRepository extends MongoRepository<ReelsNotification,String > {
    void deleteByIdAndType(String id, String type);
}
