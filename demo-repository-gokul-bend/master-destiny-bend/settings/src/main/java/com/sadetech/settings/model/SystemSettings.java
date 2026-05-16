package com.sadetech.settings.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SystemSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String homeIcon;
    private String friendRequestIcon;
    private String notificationIcon;
    private String messageIcon;
    private String widgetIcon;
    private String settingsIcon;
    private String searchIcon;
    private String logoImage;

    @ElementCollection
    @CollectionTable(name = "system_settings_colors", joinColumns = @jakarta.persistence.JoinColumn(name = "system_settings_id"))
    @MapKeyColumn(name = "color_key")
    @Column(name = "color_value")
    private Map<String, String> color;

    private List<String> footerContent;
    private String companyName;
    private String address;
    private Long phoneNumber;
    private String email;

    @ElementCollection
    @CollectionTable(name = "system_settings_social_media", joinColumns = @jakarta.persistence.JoinColumn(name = "system_settings_id"))
    @MapKeyColumn(name = "social_media_platform")
    @Column(name = "social_media_link")
    private Map<String, String> socialMediaLinks;

    private String aboutUs;
    private String privacyPolicy;
    private String termsAndConditions;
}

