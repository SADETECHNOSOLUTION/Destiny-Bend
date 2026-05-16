package com.sadetech.profession.repository;

import com.sadetech.profession.model.sub_model.PhysicalAttribute;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalAttributeRepo extends MongoRepository<PhysicalAttribute,String> {
    PhysicalAttribute findByUserId(Long userId);
}
