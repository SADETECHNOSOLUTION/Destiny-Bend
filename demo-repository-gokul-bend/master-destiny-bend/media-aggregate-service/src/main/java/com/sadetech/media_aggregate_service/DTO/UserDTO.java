package com.sadetech.media_aggregate_service.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private Long userId;

    private String name;

    private String aboutMe;
    private Date birthday;
    private String phno;
    private String bloodGroup;
    private String gender;
    private String country;
    private String occupation;
    private String email;
    private String hobbies;
    private String education;
    private String profileImagePath;
    private String bannerImagePath;


}
