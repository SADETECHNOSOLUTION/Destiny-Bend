package com.Sadetechno.jwt_module.Repository;
import com.Sadetechno.jwt_module.model.OtpEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<OtpEntity, String> {
    Optional<OtpEntity> findByEmailAndOtp(String email, String otp);
    List<OtpEntity> findAllByOrderByIdDesc();

    List<OtpEntity> findByEmailOrderByCreatedAtDesc(String email);
}



