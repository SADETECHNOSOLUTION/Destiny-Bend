package com.sadetech.profession.repository;

import com.sadetech.profession.model.ArtistPersonalInformation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistInformationRepo extends MongoRepository<ArtistPersonalInformation,String> {
    ArtistPersonalInformation findByUserId(Long userId);
}
