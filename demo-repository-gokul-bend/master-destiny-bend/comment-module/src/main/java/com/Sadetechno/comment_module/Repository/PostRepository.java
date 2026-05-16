package com.Sadetechno.comment_module.Repository;

import com.Sadetechno.comment_module.model.PostNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<PostNotification,String > {
    void deleteByIdAndType(String id, String type);
}
