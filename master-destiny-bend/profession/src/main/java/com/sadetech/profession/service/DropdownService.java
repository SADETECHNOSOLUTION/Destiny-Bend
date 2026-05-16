package com.sadetech.profession.service;

import com.sadetech.profession.model.dropdown.Dropdown;
import com.sadetech.profession.repository.DropdownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DropdownService {

    @Autowired
    private DropdownRepository dropdownRepository;

    public Dropdown addDropdownForArtistForm(Dropdown dropdown){
        return dropdownRepository.save(dropdown);
    }

    public Optional<Dropdown> getDropdownList(String id){
        return dropdownRepository.findById(id);
    }
}
