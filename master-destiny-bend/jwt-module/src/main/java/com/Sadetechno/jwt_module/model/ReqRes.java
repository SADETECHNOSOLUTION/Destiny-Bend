package com.Sadetechno.jwt_module.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String otp;
    private String otpType;
    private String emailOtpContent;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String role ;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;
    private boolean isEnabled;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;

}


