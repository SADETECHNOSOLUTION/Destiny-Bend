package com.sadetech.profession.repository;

import com.sadetech.profession.model.dropdown.Dropdown;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DropdownRepository extends MongoRepository<Dropdown,String> {
}
