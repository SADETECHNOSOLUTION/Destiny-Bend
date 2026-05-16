package com.sadetech.profession.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "professional_details")
public class ArtistPersonalInformation {

    @Id
    private String id;
    private String userType;
    private Long userId;
    private String fullName;
    private String stageName;
    private String gender;
    private Date dob;
    private String email;
    private String nationality;
    private String ethnicity;
    private String maritalStatus;
    private String languagesKnown;
    private String mobileNumber;
    private String address;
    private String nativeLanguage;
    private String emergencyContact;
    private String currentResidency;
    private String profileSummary;

}
