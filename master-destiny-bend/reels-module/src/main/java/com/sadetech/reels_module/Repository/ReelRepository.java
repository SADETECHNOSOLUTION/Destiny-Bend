package com.sadetech.reels_module.Repository;

import com.sadetech.reels_module.Model.Reel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReelRepository extends JpaRepository<Reel,Integer> {
    List<Reel> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Reel r WHERE r.userId = :userId AND r.id = :id") // JPQL - Java Persistence Query Language
    void deleteReelByUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);

    boolean existsByUserIdAndId(Long userId, Long id);

    Optional<Reel> findById(Long id);
}
