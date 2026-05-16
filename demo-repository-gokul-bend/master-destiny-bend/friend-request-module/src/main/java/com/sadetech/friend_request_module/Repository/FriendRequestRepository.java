package com.sadetech.friend_request_module.Repository;

import com.sadetech.friend_request_module.Model.FriendRequest;
import com.sadetech.friend_request_module.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {


    List<FriendRequest> findAllBySenderIdAndStatus(Long senderId, Status status);

    List<FriendRequest> findAllByRecipientIdAndStatus(Long recipientId, Status status);

    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "(fr.senderId = :senderId AND fr.recipientId = :recipientId) OR " +
            "(fr.senderId = :recipientId AND fr.recipientId = :senderId)")
    Optional<FriendRequest> findBySenderIdAndRecipientId(@Param("senderId") Long senderId,
                                                         @Param("recipientId") Long recipientId);

}
