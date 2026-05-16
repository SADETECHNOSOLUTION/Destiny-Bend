package com.sadetech.admin_login.Service;

import com.sadetech.admin_login.Repository.AdminRepo;
import com.sadetech.admin_login.Repository.OtpRepository;
import com.sadetech.admin_login.model.AdminUser;
import com.sadetech.admin_login.model.OtpEntity;
import com.sadetech.admin_login.model.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsersManagementService {

    @Autowired
    private AdminRepo usersRepo;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            // Check if the password and confirm password fields match
            if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
                resp.setStatusCode(400); // Bad Request
                resp.setMessage("Password and confirm password do not match.");
                return resp;
            }

            // Check if a user with the provided email already exists
            Optional<AdminUser> existingUser = usersRepo.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                // User already exists, return a message to log in
                resp.setStatusCode(409); // Conflict
                resp.setMessage("User already exists. Please log in.");
                return resp;
            }

            // Generate and send OTP
            String otp = generateOtp();
            String otpType = "Sign up";
            emailService.sendOtpEmail(registrationRequest.getEmail(), otp, otpType);

            registrationRequest.setRole("USER"); // Ensure the role is set to USER

            resp.setStatusCode(200);
            resp.setMessage("OTP sent to email. Please verify to complete registration.");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes verifyOtpAndRegister(ReqRes verificationRequest) {
        ReqRes resp = new ReqRes();
        try {
            // Check if OTP exists and is valid
            Optional<OtpEntity> otpEntity = otpRepository.findByEmailAndOtp(verificationRequest.getEmail(), verificationRequest.getOtp());
            if (otpEntity.isPresent()) {
                LocalDateTime otpCreatedAt = otpEntity.get().getCreatedAt();

                // Check if OTP has expired (e.g., 15 minutes expiry)
                if (LocalDateTime.now().isBefore(otpCreatedAt.plusMinutes(15))) {

                    String role = "USER";
                    // Create a new user
                    AdminUser ourUser = new AdminUser();
                    ourUser.setEmail(verificationRequest.getEmail());
                    ourUser.setPhoneNumber(verificationRequest.getPhoneNumber());
                    ourUser.setRole(role);
                    ourUser.setName(verificationRequest.getName());
                    ourUser.setPassword(passwordEncoder.encode(verificationRequest.getPassword())); // Encode password

                    AdminUser ourUsersResult = usersRepo.save(ourUser);

                    if (ourUsersResult.getId() == null) {
                        resp.setOurUsers(ourUsersResult);
                        resp.setMessage("User registered successfully");
                        resp.setStatusCode(201); // Created

                        // Remove OTP from the database
//                        otpRepository.delete(otpEntity.get());
                    }
                } else {
                    resp.setStatusCode(400); // Bad Request
                    resp.setMessage("OTP has expired.");
                }
            } else {
                resp.setStatusCode(400); // Bad Request
                resp.setMessage("Invalid OTP.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
            response.setStatusCode(HttpStatus.OK.value()); // Set status code to 200
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            AdminUser users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<AdminUser> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes requestForResetPassword(String id, String email, String newPassword,String name, String phoneNumber){
        ReqRes response = new ReqRes();

        Optional<AdminUser> getUser = usersRepo.findById(id);
        if(getUser.isPresent()) {
            AdminUser user = getUser.get();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setName(name);
            user.setPhoneNumber(phoneNumber);
            usersRepo.save(user);

            response.setStatusCode(200);
            response.setMessage("Password reset successfully");
            return response;
        }else{
            throw new IllegalArgumentException("Error in forgetting password");
        }
    }

    public long getTotalUsers() {
        return usersRepo.count();
    }

    public ReqRes requestPasswordReset(String email) {
        ReqRes response = new ReqRes();
        try {
            Optional<AdminUser> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                String otp = generateOtp();
                String otpType = "Reset-Password";
                emailService.sendOtpEmail(email, otp,otpType);

                response.setStatusCode(200);
                response.setMessage("OTP sent successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while processing request: " + e.getMessage());
        }
        return response;
    }

    public ReqRes verifyOtpAndResetPassword(String email, String otp, String newPassword, String confirmPassword) {
        ReqRes response = new ReqRes();
        try {
            // Check if OTP exists and is valid
            Optional<OtpEntity> otpEntity = otpRepository.findByEmailAndOtp(email, otp);
            if (otpEntity.isPresent()) {
                LocalDateTime otpCreatedAt = otpEntity.get().getCreatedAt();

                // Check if OTP has expired (e.g., 15 minutes expiry)
                if (LocalDateTime.now().isBefore(otpCreatedAt.plusMinutes(15))) {
                    // Check if the new password and confirm password match
                    if (newPassword.equals(confirmPassword)) {
                        // Find the user by email
                        Optional<AdminUser> userOptional = usersRepo.findByEmail(email);
                        if (userOptional.isPresent()) {
                            AdminUser user = userOptional.get();

                            // Encode and update the user's password
                            user.setPassword(passwordEncoder.encode(newPassword));
                            usersRepo.save(user); // Save only the password

                            // Remove OTP from the database (if needed)
                            otpRepository.delete(otpEntity.get());

                            response.setStatusCode(200);
                            response.setMessage("Password reset successfully");
                        } else {
                            response.setStatusCode(404);
                            response.setMessage("User not found");
                        }
                    } else {
                        response.setStatusCode(400);
                        response.setMessage("New password and confirm password do not match");
                    }
                } else {
                    response.setStatusCode(400);
                    response.setMessage("OTP has expired");
                }
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid OTP");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while resetting password: " + e.getMessage());
        }
        return response;
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public ReqRes loginWithOtp(String email, String otp) {
        ReqRes response = new ReqRes();
        try {
            Optional<OtpEntity> otpEntity = otpRepository.findByEmailAndOtp(email, otp);
            if (otpEntity.isPresent()) {
                LocalDateTime otpCreatedAt = otpEntity.get().getCreatedAt();

                // Check if OTP has expired (e.g., 15 minutes expiry)
                if (LocalDateTime.now().isBefore(otpCreatedAt.plusMinutes(5))) {
                    // Authenticate user
                    var user = usersRepo.findByEmail(email).orElseThrow();
                    var jwt = jwtUtils.generateToken(user);
                    var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

                    response.setStatusCode(200);
                    response.setToken(jwt);
                    response.setRole(user.getRole());
                    response.setRefreshToken(refreshToken);
                    response.setExpirationTime("24Hrs");
                    response.setMessage("Successfully Logged In with OTP");

                    // Remove OTP from the database
//                    otpRepository.delete(otpEntity.get());
                } else {
                    response.setStatusCode(400);
                    response.setMessage("OTP has expired");
                }
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid OTP");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Login failed: " + e.getMessage());
        }
        return response;
    }

    public List<AdminUser> getAllUsersDescending() {
        return usersRepo.findAllByOrderByIdDesc();
    }

    public List<OtpEntity> getAllOtpsDescending() {
        return otpRepository.findAllByOrderByIdDesc();

    }
    public Optional<String> findUserNameById(String id) {
        Optional<AdminUser> user = usersRepo.findById(id);
        return user.map(AdminUser::getName);
    }

    public List<OtpEntity> getOtpDetailsByEmail(String email){
        return otpRepository.findByEmailOrderByCreatedAtDesc(email);
    }

    public List<AdminUser>getAllProfiles(){
        return usersRepo.findAll();
    }

    public Optional<AdminUser> getProfileByEmail(String email){
        return usersRepo.findByEmail(email);
    }

    public boolean isEmailExist(String email){
        return usersRepo.existsByEmail(email);
    }
}
