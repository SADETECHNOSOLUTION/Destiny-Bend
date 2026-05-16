package com.sadetech.admin_login.model;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Document(collection = "otp_list")
public class OtpEntity {

    @Id
    private String id;

    private String email;
    private String otp;
    private String otpType;
    private String emailOtpContent;
    @CreatedDate
    private LocalDateTime createdAt;

    public OtpEntity(String email, String otp, String otpType, String emailOtpContent, LocalDateTime createdAt) {
        this.email = email;
        this.otp = otp;
        this.otpType = otpType;
        this.emailOtpContent = emailOtpContent;
        this.createdAt = createdAt;
    }
}
