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
@Document(collection = "artist_professional_experience")
public class ProfessionalExperience {
    @Id
    private String id;
    private Long userId;
    private int actingExperience;
    private String actingRoles;
    private String previousProjects;
    private String notableDirectorsWorkedWith;
    private String vocalRange;
    private List<String> skillsAndExpertise;
    private String danceScenes;
    private String accentsAndDialects;
    private String awardsAndAchievements;
    private List<String> methodActingTechniques;
    private String improvisationExperience;
    private String onStageAndTheatreWorks;
    private String fightAndActionScenes;
}
