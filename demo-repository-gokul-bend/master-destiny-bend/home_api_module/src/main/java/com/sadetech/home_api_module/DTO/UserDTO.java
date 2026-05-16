package com.sadetech.home_api_module.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private Long userid;

    private String name;

    private String aboutMe;
    private Date birthday;
    private String phno;
    private String bloodGroup;
    private String gender;
    private String country;
    private String occupation;
    private LocalDateTime joined;
    private String email;
    private String hobbies;
    private String education;
    private String profileImagePath;
    private String bannerImagePath;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Interest> interests;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<WorkExperience> workExperience;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "social_media_links_id")
    private SocialMediaLinks socialMediaLinks;

}
