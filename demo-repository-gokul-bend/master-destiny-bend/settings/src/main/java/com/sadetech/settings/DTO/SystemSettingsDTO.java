package com.sadetech.settings.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemSettingsDTO {
    private int id;
    private Map<String,String> color;
    private List<String> footerContent;
    private String companyName;
    private String address;
    private Long phoneNumber;
    private String email;
    private Map<String,String> socialMediaLinks;
    private String aboutUs;
    private String privacyPolicy;
    private String termsAndConditions;

}
