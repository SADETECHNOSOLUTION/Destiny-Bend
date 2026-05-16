package com.Sadetechno.like_module.Repository;

import com.Sadetechno.like_module.model.StatusNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusNotificationRepository extends MongoRepository<StatusNotification,String> {
    void deleteByIdAndType(String id, String type);
}
