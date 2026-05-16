package com.sadetech.friend_request_module.Repository;

import com.sadetech.friend_request_module.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    void deleteByIdAndType(Long id, String type);
}
