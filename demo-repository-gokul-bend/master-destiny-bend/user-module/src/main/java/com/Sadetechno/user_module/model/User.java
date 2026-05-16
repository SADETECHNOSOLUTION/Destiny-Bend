package com.Sadetechno.user_module.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userid;

    private String name;

    private String aboutMe;
    private Date birthday;
    private String phno;
    private String bloodGroup;
    private String gender;
    private String country;
    private String occupation;
    @CreationTimestamp
    private LocalDateTime joined;
    @Column(unique = true)
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

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

}