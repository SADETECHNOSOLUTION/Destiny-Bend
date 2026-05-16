package com.sadetech.profession.repository;

import com.sadetech.profession.model.sub_model.ProfessionalExperience;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalInformationRepo extends MongoRepository<ProfessionalExperience,String> {
    ProfessionalExperience findByUserId(Long userId);
}
