package com.Sadetechno.comment_module.Repository;

import com.Sadetechno.comment_module.model.StatusNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends MongoRepository<StatusNotification,String> {
    void deleteByIdAndType(String id, String type);
}
