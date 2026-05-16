package com.sadetech.profession.repository;

import com.sadetech.profession.model.sub_model.SocialMediaAccounts;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialMediaAccountsRepository extends MongoRepository<SocialMediaAccounts, String>{

    SocialMediaAccounts findByUserId(Long userId);
}