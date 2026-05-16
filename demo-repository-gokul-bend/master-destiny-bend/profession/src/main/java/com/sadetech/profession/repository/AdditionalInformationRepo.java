package com.sadetech.profession.repository;

import com.sadetech.profession.model.sub_model.AdditionalInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalInformationRepo extends MongoRepository<AdditionalInformation,String> {
    AdditionalInformation findByUserId(Long userId);
}
