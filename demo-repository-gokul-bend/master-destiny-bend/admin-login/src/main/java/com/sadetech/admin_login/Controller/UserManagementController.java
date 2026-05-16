package com.sadetech.admin_login.Controller;

import com.sadetech.admin_login.Service.JWTUtils;
import com.sadetech.admin_login.Service.OurUserDetailsService;
import com.sadetech.admin_login.Service.UsersManagementService;
import com.sadetech.admin_login.model.AdminUser;
import com.sadetech.admin_login.model.OtpEntity;
import com.sadetech.admin_login.model.ReqRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Autowired
    private JWTUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    @PostMapping("/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.register(reg));
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<ReqRes> verifyOtpAndRegister(@RequestBody ReqRes verificationRequest) {
        ReqRes response = usersManagementService.verifyOtpAndRegister(verificationRequest);

        HttpStatus status;
        switch (response.getStatusCode()) {
            case 201 -> status = HttpStatus.CREATED;

            case 400 -> status = HttpStatus.BAD_REQUEST;

            case 500 -> status = HttpStatus.INTERNAL_SERVER_ERROR;

            default -> status = HttpStatus.OK;


        }

        return new ResponseEntity<>(response, status);
    }


    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody ReqRes req) {
        try{
            ReqRes response = usersManagementService.login(req);
            return ResponseEntity.ok(response);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Un authorized user");
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }

    @GetMapping("/adminuser/get-profile")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/products/admin/count")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = usersManagementService.getTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ReqRes> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        ReqRes response = usersManagementService.requestPasswordReset(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ReqRes> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        ReqRes response = usersManagementService.verifyOtpAndResetPassword(email, otp, newPassword,confirmPassword);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/login-with-otp")
    public ResponseEntity<ReqRes> loginWithOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        ReqRes response = usersManagementService.loginWithOtp(email, otp);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/request-reset/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ReqRes>changePassword(
            @PathVariable String id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber
    ) {
        try {
            ReqRes response = usersManagementService.requestForResetPassword(id, email, newPassword, name, phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/users/descending")
    public ResponseEntity<List<AdminUser>> getUsersDescending() {
        List<AdminUser> users = usersManagementService.getAllUsersDescending();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/otps/descending")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<List<OtpEntity>> getOtpsDescending() {
        List<OtpEntity> otps = usersManagementService.getAllOtpsDescending();
        return ResponseEntity.ok(otps);
    }
    @GetMapping("/user/{id}/username")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<String> getUserNameById(@PathVariable String id) {
        Optional<String> userName = usersManagementService.findUserNameById(id);
        return userName.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @GetMapping("/otp-details/{email}")
    public ResponseEntity<List<OtpEntity>> getOtpDetails(@PathVariable String email){
        try {
            List<OtpEntity> otpEntity = usersManagementService.getOtpDetailsByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(otpEntity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get-username/{email}")
    public ResponseEntity<AdminUser> getDetails(@PathVariable(name = "email") String username){
        try {
            AdminUser adminUser = ourUserDetailsService.loadUserByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(adminUser);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/all-profiles")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<AdminUser>> getAllProfiles(){
        try {
            List<AdminUser> ourUsers = usersManagementService.getAllProfiles();
            return ResponseEntity.status(HttpStatus.OK).body(ourUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get-profile/{email}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Optional<AdminUser>> getProfileByEmail(@PathVariable String email){
        try {
            Optional<AdminUser> ourUsers =  usersManagementService.getProfileByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(ourUsers);
        } catch (Exception e) {
            logger.warn("un Authorized {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

        @PostMapping("/check-email/{email}")
        public ResponseEntity<?> checkEmail(@PathVariable String email) {
            boolean emailExists = usersManagementService.isEmailExist(email);

            if (emailExists) {
                return ResponseEntity.ok("Email exists");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Email not found");
            }
        }

}
