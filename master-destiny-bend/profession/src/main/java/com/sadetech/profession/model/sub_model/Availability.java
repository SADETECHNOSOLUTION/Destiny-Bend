package com.sadetech.profession.model.sub_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "artist_availability")
public class Availability {

    @Id
    private String id;
    private Long userId;
    private Date availableDates;
    private String preferredWorkLocation;
    private String availabilityForWorkShops;
    private List<String> workTypePreference;
    private String contractType;
    private String willingnessToTravel;
    private boolean availabilityForNightShoots;
    private String longTermCommitment;
    private String workScheduleFlexibility;
    private List<String> roleTypePreference;
}
