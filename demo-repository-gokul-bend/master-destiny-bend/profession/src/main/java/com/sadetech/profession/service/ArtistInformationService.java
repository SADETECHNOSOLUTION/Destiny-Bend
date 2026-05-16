package com.sadetech.profession.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadetech.profession.dto.AdditionalInformationDTO;
import com.sadetech.profession.dto.ArtistInformationDTO;
import com.sadetech.profession.model.ArtistPersonalInformation;
import com.sadetech.profession.model.sub_model.*;
import com.sadetech.profession.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistInformationService {

    @Autowired
    private ArtistInformationRepo artistPersonalInformationRepository;

    @Autowired
    private SocialMediaAccountsRepository socialMediaAccountsRepository;

    @Autowired
    private ProfessionalInformationRepo professionalExperienceRepository;

    @Autowired
    private PhysicalAttributeRepo physicalAttributeRepository;

    @Autowired
    private AvailabilityRepo availabilityRepository;

    @Autowired
    private AdditionalInformationRepo additionalInformationRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(ArtistInformationService.class);

    public ArtistPersonalInformation addPersonalInformation(ArtistPersonalInformation artistPersonalInformation){
        return artistPersonalInformationRepository.save(artistPersonalInformation);
    }

    public ArtistPersonalInformation addArtistPersonalInformation(ArtistPersonalInformation artistPersonalInformation) {
        // Retrieve existing artist information by userId
        ArtistPersonalInformation existingArtistInformation = artistPersonalInformationRepository.findByUserId(artistPersonalInformation.getUserId());

        // If no existing record is found, create a new one
        if (existingArtistInformation == null) {
            existingArtistInformation = new ArtistPersonalInformation();
            existingArtistInformation.setUserId(artistPersonalInformation.getUserId());
        }

        // Update fields if the incoming data is not null
        if (artistPersonalInformation.getFullName() != null) {
            existingArtistInformation.setFullName(artistPersonalInformation.getFullName());
        }
        if (artistPersonalInformation.getStageName() != null) {
            existingArtistInformation.setStageName(artistPersonalInformation.getStageName());
        }
        if (artistPersonalInformation.getGender() != null) {
            existingArtistInformation.setGender(artistPersonalInformation.getGender());
        }
        if (artistPersonalInformation.getDob() != null) {
            existingArtistInformation.setDob(artistPersonalInformation.getDob());
        }
        if (artistPersonalInformation.getEmail() != null) {
            existingArtistInformation.setEmail(artistPersonalInformation.getEmail());
        }
        if (artistPersonalInformation.getNationality() != null) {
            existingArtistInformation.setNationality(artistPersonalInformation.getNationality());
        }
        if (artistPersonalInformation.getEthnicity() != null) {
            existingArtistInformation.setEthnicity(artistPersonalInformation.getEthnicity());
        }
        if (artistPersonalInformation.getMaritalStatus() != null) {
            existingArtistInformation.setMaritalStatus(artistPersonalInformation.getMaritalStatus());
        }
        if (artistPersonalInformation.getLanguagesKnown() != null) {
            existingArtistInformation.setLanguagesKnown(artistPersonalInformation.getLanguagesKnown());
        }
        if (artistPersonalInformation.getMobileNumber() != null) {
            existingArtistInformation.setMobileNumber(artistPersonalInformation.getMobileNumber());
        }
        if (artistPersonalInformation.getAddress() != null) {
            existingArtistInformation.setAddress(artistPersonalInformation.getAddress());
        }
        if (artistPersonalInformation.getNativeLanguage() != null) {
            existingArtistInformation.setNativeLanguage(artistPersonalInformation.getNativeLanguage());
        }
        if (artistPersonalInformation.getEmergencyContact() != null) {
            existingArtistInformation.setEmergencyContact(artistPersonalInformation.getEmergencyContact());
        }
        if (artistPersonalInformation.getCurrentResidency() != null) {
            existingArtistInformation.setCurrentResidency(artistPersonalInformation.getCurrentResidency());
        }
        if (artistPersonalInformation.getProfileSummary() != null) {
            existingArtistInformation.setProfileSummary(artistPersonalInformation.getProfileSummary());
        }

        // Save the artist information (new or updated)
        return artistPersonalInformationRepository.save(existingArtistInformation);
    }



    public SocialMediaAccounts addSocialMediaAccounts(SocialMediaAccounts socialMediaAccounts){
        return socialMediaAccountsRepository.save(socialMediaAccounts);
    }

    public ProfessionalExperience addProfessionalExperience(ProfessionalExperience professionalExperience){
        return professionalExperienceRepository.save(professionalExperience);
    }

    public PhysicalAttribute addPhysicalAttribute(PhysicalAttribute physicalAttribute){
        return physicalAttributeRepository.save(physicalAttribute);
    }

    public Availability addAvailability(Availability availability){
        return availabilityRepository.save(availability);
    }

    public AdditionalInformation addAdditionalInformationDto(AdditionalInformationDTO additionalInformationDTO) throws IOException {
        if (additionalInformationDTO == null) {
            throw new IllegalArgumentException("Additional information is null");
        }

        String userJson = additionalInformationDTO.getAdditionalInfoJson();
        if (userJson == null || userJson.isEmpty()) {
            throw new IllegalArgumentException("userJson is null or empty");
        }

        AdditionalInformation additionalInformation;
        try {
            additionalInformation = objectMapper.readValue(userJson, AdditionalInformation.class);
        } catch (IOException e) {
            throw new IOException("Error deserializing userJson: " + e.getMessage(), e);
        }

        // Process profile image
        if (additionalInformationDTO.getPortfolioAndWorkSample() != null && !additionalInformationDTO.getPortfolioAndWorkSample().isEmpty()) {
            try {
                String portfolioImagePath = fileUploadService.uploadFile(additionalInformationDTO.getPortfolioAndWorkSample());
                additionalInformation.setPortFolioAndWorkSample(portfolioImagePath);
            } catch (IOException e) {
                throw new IOException("Error uploading profile image: " + e.getMessage(), e);
            }
        }

        // Save to DB
        try {
            // Save the AdditionalInformation object to the database
            additionalInformation = additionalInformationRepository.save(additionalInformation);

            return additionalInformation;
        } catch (Exception e) {
            throw new IOException("Error saving to the database: " + e.getMessage(), e);
        }
    }

    public ArtistInformationDTO getDetailsByUserId(Long userId) {
        
        // Fetch details using repository methods
        ArtistPersonalInformation artistPersonalInfo = artistPersonalInformationRepository.findByUserId(userId);
        SocialMediaAccounts socialMediaAccounts = socialMediaAccountsRepository.findByUserId(userId);
        ProfessionalExperience professionalExperience = professionalExperienceRepository.findByUserId(userId);
        PhysicalAttribute physicalAttribute = physicalAttributeRepository.findByUserId(userId);
        Availability availability = availabilityRepository.findByUserId(userId);
        AdditionalInformation additionalInformation = additionalInformationRepository.findByUserId(userId);

        return new ArtistInformationDTO(
                artistPersonalInfo,
                socialMediaAccounts,
                professionalExperience,
                physicalAttribute,
                availability,
                additionalInformation
        );
    }

    @Transactional
    public ProfessionalExperience updateSkillsAndExpertise(Long userId, String newSkill, boolean isAdd) {
        ProfessionalExperience professionalExperience = professionalExperienceRepository.findByUserId(userId);

        if (professionalExperience == null) {
            logger.warn("No ProfessionalExperience found for userId: {}", userId);
            throw new IllegalArgumentException("No data found.");
        }

        logger.info("Existing ProfessionalExperience: {}", professionalExperience);
        logger.info("New skill to add: {}", newSkill);

        List<String> skills = professionalExperience.getSkillsAndExpertise();
        if (skills == null) {
            skills = new ArrayList<>();
            professionalExperience.setSkillsAndExpertise(skills);
            logger.info("Initialized skillsAndExpertise list.");
        }

        if (isAdd && !skills.contains(newSkill)) {
            skills.add(newSkill);
            logger.info("Updated skills list: {}", skills);
        } else {
            logger.info("Skill '{}' already exists in the list.", newSkill);
        }

        if(!isAdd && skills.contains(newSkill)){
            skills.remove(newSkill);
        }else {
            logger.info("Skill '{}' does not exists in the list", newSkill);
        }

        return professionalExperienceRepository.save(professionalExperience);
    }

    @Transactional
    public ProfessionalExperience updateMethodActingTechniques(Long userId, String newTechniques, boolean isAdd) {
        ProfessionalExperience professionalExperience = professionalExperienceRepository.findByUserId(userId);

        if (professionalExperience == null) {
            throw new IllegalArgumentException("No data found.");
        }

        List<String> skills = professionalExperience.getMethodActingTechniques();
        if (skills == null) {
            skills = new ArrayList<>();
            professionalExperience.setMethodActingTechniques(skills);
        }

        if (isAdd && !skills.contains(newTechniques)) {
            skills.add(newTechniques);
        } else {
            logger.info("Techniques '{}' already exists in the list.", newTechniques);
        }

        if(!isAdd && skills.contains(newTechniques)){
            skills.remove(newTechniques);
        }else {
            logger.info("Techniques '{}' does not exists in the list", newTechniques);
        }

        return professionalExperienceRepository.save(professionalExperience);
    }

    @Transactional
    public AdditionalInformation updateHobbies(Long userId, String hobbies, boolean isAdd) {
        AdditionalInformation additionalInformation = additionalInformationRepository.findByUserId(userId);

        if (additionalInformation == null) {
            throw new IllegalArgumentException("No data found.");
        }

        List<String> skills = additionalInformation.getHobbiesAndInterests();
        if (skills == null) {
            skills = new ArrayList<>();
            additionalInformation.setHobbiesAndInterests(skills);
        }

        if (isAdd && !skills.contains(hobbies)) {
            skills.add(hobbies);
        } else {
            logger.info("Hobbies '{}' already exists in the list.", hobbies);
        }

        if(!isAdd && skills.contains(hobbies)){
            skills.remove(hobbies);
        }else {
            logger.info("Hobbies '{}' does not exists in the list", hobbies);
        }

        return additionalInformationRepository.save(additionalInformation);
    }

    @Transactional
    public Availability updateWorkTypePreference(Long userId, String workPreference, boolean isAdd) {
        Availability availability = availabilityRepository.findByUserId(userId);

        if (availability == null) {
            logger.info("No details found");
            throw new IllegalArgumentException("No data found.");
        }

        List<String> skills = availability.getWorkTypePreference();
        if (skills == null) {
            skills = new ArrayList<>();
            availability.setWorkTypePreference(skills);
            logger.info("Work preference added : {}",skills);
        }

        if (isAdd && !skills.contains(workPreference)) {
            skills.add(workPreference);
            logger.info("Work preference list after adding {}",skills);
        } else {
            logger.info("Preference '{}' already exists in the list.", workPreference);
        }

        if(!isAdd && skills.contains(workPreference)){
            skills.remove(workPreference);
            logger.info("Work preference after removing the requested one {}",skills);
        }else {
            logger.info("Work preference '{}' does not exists in the list", workPreference);
        }

        return availabilityRepository.save(availability);
    }

}
