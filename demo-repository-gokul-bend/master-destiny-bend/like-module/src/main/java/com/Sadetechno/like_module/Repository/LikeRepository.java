package com.Sadetechno.like_module.Repository;


import com.Sadetechno.like_module.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, String > {
    List<Like> findByPostId(Long postId);

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
//    @Query("SELECT l.userId FROM Like l WHERE l.postId = :postId")
    List<Long> findUserIdByPostId(@Param("postId") Long postId);

}


