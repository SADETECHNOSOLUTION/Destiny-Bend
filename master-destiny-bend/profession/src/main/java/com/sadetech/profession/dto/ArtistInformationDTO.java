package com.sadetech.profession.dto;

import com.sadetech.profession.model.ArtistPersonalInformation;
import com.sadetech.profession.model.sub_model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistInformationDTO {

    private ArtistPersonalInformation artistPersonalInformation;
    private SocialMediaAccounts socialMediaAccounts;
    private ProfessionalExperience professionalExperience;
    private PhysicalAttribute physicalAttribute;
    private Availability availability;
    private AdditionalInformation additionalInformation;

}
