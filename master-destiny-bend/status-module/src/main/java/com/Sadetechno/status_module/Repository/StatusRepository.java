package com.Sadetechno.status_module.Repository;

import com.Sadetechno.status_module.Dto.StatusResponseDTO;
import com.Sadetechno.status_module.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Long> {
    List<Status> findByUserId(Long userId);
    List<Status> findByCreatedAtBefore(LocalDateTime cutoff);

    Optional<Status> findByUserIdAndId(Long userId, Long id);

    void deleteStatusByUserIdAndId( Long userId, Long id);

    Optional<StatusResponseDTO> findUserDetailsById(Long id);
}
