package com.sadetech.profession.controller;
import com.sadetech.profession.dto.AdditionalInformationDTO;
import com.sadetech.profession.dto.ArtistInformationDTO;
import com.sadetech.profession.model.ArtistPersonalInformation;
import com.sadetech.profession.model.dropdown.Dropdown;
import com.sadetech.profession.model.sub_model.*;
import com.sadetech.profession.service.ArtistInformationService;
import com.sadetech.profession.service.DropdownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/profession")
public class ArtistInformationController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistInformationController.class);  // Initialize logger

    @Autowired
    private ArtistInformationService artistInformationService;

    @Autowired
    private DropdownService dropdownService;

    @PostMapping("/personal-info")
    public ResponseEntity<?> addPersonalDetails(@RequestBody ArtistPersonalInformation artistPersonalInformation) {
        try {
            ArtistPersonalInformation artistPersonalInformation1 = artistInformationService.addPersonalInformation(artistPersonalInformation);
            return ResponseEntity.status(HttpStatus.CREATED).body(artistPersonalInformation1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding details");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details, check logs..");
        }
    }

    @PostMapping("/social-media-accounts")
    public ResponseEntity<?> addSocialMediaAccounts(@RequestBody SocialMediaAccounts socialMediaAccounts) {
        try {
            SocialMediaAccounts socialMediaAccounts1 = artistInformationService.addSocialMediaAccounts(socialMediaAccounts);
            return ResponseEntity.status(HttpStatus.CREATED).body(socialMediaAccounts1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding details");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details, check logs..");
        }
    }

    @PostMapping("/professional-experience")
    public ResponseEntity<?> addProfessionalExperience(@RequestBody ProfessionalExperience professionalExperience) {
        try {
            ProfessionalExperience professionalExperience1 = artistInformationService.addProfessionalExperience(professionalExperience);
            return ResponseEntity.status(HttpStatus.CREATED).body(professionalExperience1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding details");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details, check logs..");
        }
    }

    @PostMapping("/physical-attribute")
    public ResponseEntity<?> addPhysicalAttribute(@RequestBody PhysicalAttribute physicalAttribute) {
        try {
            PhysicalAttribute physicalAttribute1 = artistInformationService.addPhysicalAttribute(physicalAttribute);
            return ResponseEntity.status(HttpStatus.CREATED).body(physicalAttribute1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding details");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details, check logs..");
        }
    }

    @PostMapping("/availability")
    public ResponseEntity<?> addAvailabilityOfPerson(@RequestBody Availability availability) {
        try {
            Availability availability1 = artistInformationService.addAvailability(availability);
            return ResponseEntity.status(HttpStatus.CREATED).body(availability1);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding details");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details, check logs..");
        }
    }

    @PostMapping("/additional-information")
    public ResponseEntity<?> createUserWithImages(@RequestParam("additionalInfoJson") String additionalInfoJson,
                                                  @RequestParam("portfolioAndWorkSample") MultipartFile portfolioAndWorkSample) {
        try {
            AdditionalInformationDTO additionalInformationDTO = new AdditionalInformationDTO();
            additionalInformationDTO.setAdditionalInfoJson(additionalInfoJson);
            additionalInformationDTO.setPortfolioAndWorkSample(portfolioAndWorkSample);
            AdditionalInformation additionalInformation = artistInformationService.addAdditionalInformationDto(additionalInformationDTO);
            return ResponseEntity.status(HttpStatus.OK).body("User created successfully with ID: " + additionalInformation.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-details/{userId}")
    public ResponseEntity<ArtistInformationDTO> getAllDetailsOfArtist(@PathVariable Long userId) {
        try {
            ArtistInformationDTO artistInformationDTO = artistInformationService.getDetailsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(artistInformationDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/add-dropdown/artist")
    public Dropdown addDropdownForArtist(@RequestBody Dropdown dropdown) {
        try {
            return dropdownService.addDropdownForArtistForm(dropdown);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't upload details");
        }
    }

    @PatchMapping("/{userId}/skills")
    public ResponseEntity<ProfessionalExperience> addSkill(
            @PathVariable Long userId,
            @RequestParam String newSkill,
            @RequestParam boolean isAdd) {
        ProfessionalExperience updatedExperience = artistInformationService.updateSkillsAndExpertise(userId, newSkill,isAdd);
        return ResponseEntity.ok(updatedExperience);
    }

    @PatchMapping("/{userId}/hobbies")
    public ResponseEntity<AdditionalInformation> addHobbies(
            @PathVariable Long userId,
            @RequestParam String hobbies,
            @RequestParam boolean isAdd) {
        AdditionalInformation updatedExperience = artistInformationService.updateHobbies(userId, hobbies,isAdd);
        return ResponseEntity.ok(updatedExperience);
    }

    @PatchMapping("/{userId}/techniques")
    public ResponseEntity<ProfessionalExperience> addNewSkill(
            @PathVariable Long userId,
            @RequestParam String newTechniques,
            @RequestParam boolean isAdd) {
        ProfessionalExperience updatedExperience = artistInformationService.updateMethodActingTechniques(userId, newTechniques,isAdd);
        return ResponseEntity.ok(updatedExperience);
    }

    @PatchMapping("/{userId}/work-preference")
    public ResponseEntity<Availability> addWorkPreference(
            @PathVariable Long userId,
            @RequestParam String workPreference,
            @RequestParam boolean isAdd) {
        Availability updatedExperience = artistInformationService.updateWorkTypePreference(userId, workPreference,isAdd);
        return ResponseEntity.ok(updatedExperience);
    }

    @PostMapping("/add-or-update")
    public ResponseEntity<ArtistPersonalInformation> addOrUpdateArtistPersonalInformation(
            @RequestBody ArtistPersonalInformation artistPersonalInformation) {
        ArtistPersonalInformation updatedArtistInfo = artistInformationService
                .addArtistPersonalInformation(artistPersonalInformation);
        return new ResponseEntity<>(updatedArtistInfo, HttpStatus.OK);
    }

    @GetMapping("/get-dropdown/{id}")
    public ResponseEntity<Optional<Dropdown>> getDropdown(@PathVariable String id){
        Optional<Dropdown> dropdown = dropdownService.getDropdownList(id);
        return ResponseEntity.ok(dropdown);
    }

}