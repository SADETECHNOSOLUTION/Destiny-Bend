package com.sadetech.profession.model.dropdown;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "artist_form_dropdown")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dropdown {

    @Id
    private String id;
    private List<String> gender;
    private List<String> nationality;
    private List<String> ethnicity;
    private List<String> maritalStatus;
    private List<String> languagesKnown;
    private List<String> currentResidencyStatus;
    private List<String> bodyType;
    private List<String> hairColor;
    private List<String> hairLength;
    private List<String> eyeColor;
    private List<String> skinTone;
    private List<String> facialHair;
    private List<String> clothingSize;
    private List<String> build;
    private List<String> voiceType;
    private List<String> actingRoles;
    private List<String> skillsAndExpertise;
    private List<String> languageSpokenFluently;
    private List<String> methodActingTechniques;
    private List<String> vocalRange;
    private List<String> preferredWorkLocation;
    private List<String> workTypePreference;
    private List<String> contractType;
    private List<String> longTermCommitment;
    private List<String> workScheduleFlexibility;
    private List<String> roleTypePreference;

}
