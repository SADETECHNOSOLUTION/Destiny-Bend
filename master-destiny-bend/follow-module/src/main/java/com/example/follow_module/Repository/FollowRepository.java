package com.example.follow_module.Repository;

import com.example.follow_module.Model.FollowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowModel, Long> {

    Optional<FollowModel> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<FollowModel> findAllByFollowerId(Long followerId); // to get all users the follower is following

    List<FollowModel> findAllByFollowingId(Long followingId); // to get all followers of a user
}

