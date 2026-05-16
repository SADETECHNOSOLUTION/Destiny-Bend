package com.sadetech.admin_login.Repository;


import com.sadetech.admin_login.model.AdminUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepo extends MongoRepository<AdminUser, String> {

    Optional<AdminUser> findByEmail(String email);
    List<AdminUser> findAllByOrderByIdDesc();


    boolean existsByEmail(String email);
}