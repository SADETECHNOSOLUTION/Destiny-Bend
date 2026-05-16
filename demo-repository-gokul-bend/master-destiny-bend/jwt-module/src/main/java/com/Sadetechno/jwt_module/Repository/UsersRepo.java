package com.Sadetechno.jwt_module.Repository;


import com.Sadetechno.jwt_module.model.OurUsers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends MongoRepository<OurUsers, String> {

    Optional<OurUsers> findByEmail(String email);
    List<OurUsers> findAllByOrderByIdDesc();


}