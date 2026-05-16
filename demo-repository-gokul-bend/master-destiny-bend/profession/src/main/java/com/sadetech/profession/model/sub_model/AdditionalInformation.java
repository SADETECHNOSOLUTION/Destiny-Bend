package com.sadetech.profession.model.sub_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "artist_additional_information")
public class AdditionalInformation {

    @Id
    private String id;
    private Long userId;
    private String portFolioAndWorkSample;
    private String references;
    private String socialMediaResearch;
    private String equipmentAndPropsOwned;
    private String specialRequirements;
    private String medicalCondition;
    private String careerGoals;
    private String membershipsAndAffiliations;
    private List<String> hobbiesAndInterests;
    private String publicSpeakingExperience;
    private String personalTransportationAvailability;

}