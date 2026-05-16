package com.Sadetechno.comment_module.Repository;

import com.Sadetechno.comment_module.model.ReelsNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReelsRepository extends MongoRepository<ReelsNotification,String > {
    void deleteByIdAndType(String id, String type);
}
