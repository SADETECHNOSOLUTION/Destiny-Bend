package com.example.follow_module.Repository;

import com.example.follow_module.Model.FollowModel;
import com.example.follow_module.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    void deleteByIdAndType(Long id, String type);
}
