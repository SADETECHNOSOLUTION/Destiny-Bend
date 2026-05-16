package com.Sadetechno.jwt_module.Service;
import com.Sadetechno.jwt_module.DTO.UserDTO;
import com.Sadetechno.jwt_module.FeignClient.UserFeignClient;
import com.Sadetechno.jwt_module.Repository.OtpRepository;
import com.Sadetechno.jwt_module.Repository.UsersRepo;
import com.Sadetechno.jwt_module.model.OtpEntity;
import com.Sadetechno.jwt_module.model.OurUsers;
import com.Sadetechno.jwt_module.model.ReqRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;

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

    @Autowired
    private UserFeignClient userFeignClient;

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
            Optional<OurUsers> existingUser = usersRepo.findByEmail(registrationRequest.getEmail());
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
                    OurUsers ourUser = new OurUsers();
                    ourUser.setEmail(verificationRequest.getEmail());
                    ourUser.setPhoneNumber(verificationRequest.getPhoneNumber());
                    ourUser.setRole(role);
                    ourUser.setName(verificationRequest.getName());
                    ourUser.setPassword(passwordEncoder.encode(verificationRequest.getPassword())); // Encode password

                    OurUsers ourUsersResult = usersRepo.save(ourUser);

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
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
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
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
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

    public long getTotalUsers() {
        return usersRepo.count();
    }

    public ReqRes requestPasswordReset(String email) {
        ReqRes response = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
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
                        Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
                        if (userOptional.isPresent()) {
                            OurUsers user = userOptional.get();

                            // Encode and update the user's password
                            user.setPassword(passwordEncoder.encode(newPassword));
                            usersRepo.save(user); // Save only the password

                            // Remove OTP from the database (if needed)
//                            otpRepository.delete(otpEntity.get());

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

    public ReqRes requestForResetPassword(Long userid, String id, String email, String newPassword, String name, String phoneNumber) {
        ReqRes response = new ReqRes();

        Optional<OurUsers> getUser = usersRepo.findById(id);
        if (getUser.isPresent()) {
            OurUsers user = getUser.get();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setName(name);
            user.setPhoneNumber(phoneNumber);

            try {
                // Make the Feign call and capture the response
                UserDTO updatedUser = userFeignClient.updateEmailAndPhoneNumber(userid, email, phoneNumber);
                // Optionally, log the result or perform further actions with the response
                if (updatedUser != null) {
                    // Log or process the updated user details if necessary
                    System.out.println("Updated user in User service: " + updatedUser);
                }
            } catch (Exception e) {
                // Handle any errors that might occur with the Feign client
                response.setStatusCode(500);
                response.setMessage("Failed to update user info in User microservice");
                return response;
            }

            // Save the changes in the JWT microservice
            usersRepo.save(user);

            // Return success response
            response.setStatusCode(200);
            response.setMessage("Password and user information reset successfully");
            return response;
        } else {
            throw new IllegalArgumentException("Error in updating password, user not found");
        }
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

    public List<OurUsers> getAllUsersDescending() {
        return usersRepo.findAllByOrderByIdDesc();
    }

    public List<OtpEntity> getAllOtpsDescending() {
        return otpRepository.findAllByOrderByIdDesc();
    }
    public Optional<String> findUserNameById(String id) {
        Optional<OurUsers> user = usersRepo.findById(id);
        return user.map(OurUsers::getName);
    }

    public List<OtpEntity> getOtpDetailsByEmail(String email){
        return otpRepository.findByEmailOrderByCreatedAtDesc(email);
    }

    public List<OurUsers>getAllProfiles(){
        return usersRepo.findAll();
    }

    public Optional<OurUsers> getProfileByEmail(String email){
        log.info("Email found for the user {}",email);
        return usersRepo.findByEmail(email);
    }

}
