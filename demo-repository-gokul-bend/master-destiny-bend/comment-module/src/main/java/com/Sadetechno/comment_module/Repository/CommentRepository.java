package com.Sadetechno.comment_module.Repository;

import com.Sadetechno.comment_module.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String > {
    List<Comment> findByParentCommentId(Long parentId);
    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    List<Comment> findByPostId(Long postId);

}
