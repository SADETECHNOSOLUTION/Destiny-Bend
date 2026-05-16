package com.sadetech.profession.repository;

import com.sadetech.profession.model.sub_model.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepo extends MongoRepository<Availability,String> {
    Availability findByUserId(Long userId);
}
