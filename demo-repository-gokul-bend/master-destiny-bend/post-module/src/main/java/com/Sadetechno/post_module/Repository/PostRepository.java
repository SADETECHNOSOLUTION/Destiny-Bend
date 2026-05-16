package com.Sadetechno.post_module.Repository;
import com.Sadetechno.post_module.model.Post;
import com.Sadetechno.post_module.model.PostType;
import com.Sadetechno.post_module.model.PostVisibility;
import com.Sadetechno.post_module.model.PrivacySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Post> findByUserIdAndPostTypeOrderByCreatedAtDesc(Long userId, PostType postType);


    Optional<Post> findByUserId(Long userId);

    List<Post> findByUserIdAndPostVisibility(Long userId, PostVisibility postVisibility);

    Post findByPostIdAndPrivacySetting(Long userId, PrivacySetting privacySetting);
}

