package com.Sadetechno.user_module.Repository;

import com.Sadetechno.user_module.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;




public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(Long userid);

    Optional<User> findByEmail(String email);
}
