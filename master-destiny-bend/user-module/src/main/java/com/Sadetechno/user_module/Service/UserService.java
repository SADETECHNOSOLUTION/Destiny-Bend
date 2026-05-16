package com.Sadetechno.user_module.Service;

import com.Sadetechno.user_module.DTO.BannerDTO;
import com.Sadetechno.user_module.DTO.ProfileDTO;
import com.Sadetechno.user_module.Repository.UserRepository;
import com.Sadetechno.user_module.model.User;
import com.Sadetechno.user_module.model.UserCreationDTO;
import com.Sadetechno.user_module.model.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final ObjectMapper objectMapper;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findByUserid(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public User createUserWithImages(UserCreationDTO userCreationDTO) throws IOException {
        if (userCreationDTO == null) {
            throw new IllegalArgumentException("UserCreationDTO is null");
        }

        String userJson = userCreationDTO.getUserJson();
        if (userJson == null || userJson.isEmpty()) {
            throw new IllegalArgumentException("userJson is null or empty");
        }

        User user;
        try {
            user = objectMapper.readValue(userJson, User.class);
        } catch (IOException e) {
            log.error("Error deserializing userJson", e);
            throw new IOException("Error deserializing userJson: " + e.getMessage(), e);
        }

        // Set the visibility
        user.setVisibility(userCreationDTO.getVisibility());

        // Process profile image
        if (userCreationDTO.getProfileImage() != null && !userCreationDTO.getProfileImage().isEmpty()) {
            try {
                String profileImagePath = fileUploadService.uploadFile(userCreationDTO.getProfileImage());
                user.setProfileImagePath(profileImagePath);
            } catch (IOException e) {
                log.error("Error uploading profile image", e);
                throw new IOException("Error uploading profile image: " + e.getMessage(), e);
            }
        }

        // Process banner image
        if (userCreationDTO.getBannerImage() != null && !userCreationDTO.getBannerImage().isEmpty()) {
            try {
                String bannerImagePath = fileUploadService.uploadFile(userCreationDTO.getBannerImage());
                user.setBannerImagePath(bannerImagePath);
            } catch (IOException e) {
                log.error("Error uploading banner image", e);
                throw new IOException("Error uploading banner image: " + e.getMessage(), e);
            }
        }

        try {
            // Save the user to generate the id
            User savedUser = userRepository.save(user);

            // Now set the userid to the generated id
            savedUser.setUserid(savedUser.getId());

            // Save again to persist the userid
            savedUser = userRepository.save(savedUser);
            log.info("Saved User: {}", savedUser);
            return savedUser;

        } catch (Exception e) {
            log.error("Unexpected error saving user", e);
            throw new RuntimeException("Unexpected error saving user: " + e.getMessage(), e);
        }
    }


    public User updateUser(Long id, UserCreationDTO userUpdateDTO) throws IOException {
        Optional<User> existingUserOptional = userRepository.findByUserid(id);
        if (!existingUserOptional.isPresent()) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User existingUser = existingUserOptional.get();

        if (userUpdateDTO == null) {
            throw new IllegalArgumentException("UserCreationDTO is null");
        }

        String userJson = userUpdateDTO.getUserJson();
        if (userJson != null && !userJson.isEmpty()) {
            try {
                User updatedUserData = objectMapper.readValue(userJson, User.class);

                // Update fields from updatedUserData to existingUser
                if (updatedUserData.getName() != null) existingUser.setName(updatedUserData.getName());
                if (updatedUserData.getAboutMe() != null) existingUser.setAboutMe(updatedUserData.getAboutMe());
                if (updatedUserData.getBirthday() != null) existingUser.setBirthday(updatedUserData.getBirthday());
                if (updatedUserData.getPhno() != null) existingUser.setPhno(updatedUserData.getPhno());
                if (updatedUserData.getBloodGroup() != null) existingUser.setBloodGroup(updatedUserData.getBloodGroup());
                if (updatedUserData.getGender() != null) existingUser.setGender(updatedUserData.getGender());
                if (updatedUserData.getCountry() != null) existingUser.setCountry(updatedUserData.getCountry());
                if (updatedUserData.getOccupation() != null) existingUser.setOccupation(updatedUserData.getOccupation());
                if (updatedUserData.getJoined() != null) existingUser.setJoined(updatedUserData.getJoined());
                if (updatedUserData.getEmail() != null) existingUser.setEmail(updatedUserData.getEmail());
                if (updatedUserData.getHobbies() != null) existingUser.setHobbies(updatedUserData.getHobbies());
                if (updatedUserData.getEducation() != null) existingUser.setEducation(updatedUserData.getEducation());
                // Update interests, workExperience, and socialMediaLinks if they are not null
                if (updatedUserData.getInterests() != null) existingUser.setInterests(updatedUserData.getInterests());
                if (updatedUserData.getWorkExperience() != null) existingUser.setWorkExperience(updatedUserData.getWorkExperience());
                if (updatedUserData.getSocialMediaLinks() != null) existingUser.setSocialMediaLinks(updatedUserData.getSocialMediaLinks());

                // Update visibility if provided
                if (userUpdateDTO.getVisibility() != null) {
                    existingUser.setVisibility(userUpdateDTO.getVisibility());
                }
            } catch (IOException e) {
                log.error("Error deserializing userJson", e);
                throw new IOException("Error deserializing userJson: " + e.getMessage(), e);
            }
        }

        // Update profile image
        if (userUpdateDTO.getProfileImage() != null && !userUpdateDTO.getProfileImage().isEmpty()) {
            try {
                String profileImagePath = fileUploadService.uploadFile(userUpdateDTO.getProfileImage());
                existingUser.setProfileImagePath(profileImagePath);
            } catch (IOException e) {
                log.error("Error uploading profile image", e);
                throw new IOException("Error uploading profile image: " + e.getMessage(), e);
            }
        }

        // Update banner image
        if (userUpdateDTO.getBannerImage() != null && !userUpdateDTO.getBannerImage().isEmpty()) {
            try {
                String bannerImagePath = fileUploadService.uploadFile(userUpdateDTO.getBannerImage());
                existingUser.setBannerImagePath(bannerImagePath);
            } catch (IOException e) {
                log.error("Error uploading banner image", e);
                throw new IOException("Error uploading banner image: " + e.getMessage(), e);
            }
        }

        try {
            User savedUser = userRepository.save(existingUser);
            log.info("Updated User: {}", savedUser);
            return savedUser;
        } catch (Exception e) {
            log.error("Unexpected error updating user", e);
            throw new RuntimeException("Unexpected error updating user: " + e.getMessage(), e);
        }
    }

    public User updateProfileImage(Long id, ProfileDTO profileDTO) throws IOException {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Upload the new profile image and update the path
            String newProfileImagePath = fileUploadService.uploadFile(profileDTO.getProfileImagePath());
            user.setProfileImagePath(newProfileImagePath);

            return userRepository.save(user); // Save the updated user
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public User updateBannerImage(Long id, BannerDTO bannerDTO) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
               User user = optionalUser.get();

              String newBannerImagePath = fileUploadService.uploadFile(bannerDTO.getBannerImagePath());
              user.setBannerImagePath(newBannerImagePath);

              return userRepository.save(user);
        }else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public User updateUserVisibility(Long id, Visibility visibility) {
        Optional<User> existingUserOptional = userRepository.findByUserid(id);
        if (!existingUserOptional.isPresent()) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }

        User existingUser = existingUserOptional.get();
        existingUser.setVisibility(visibility);

        try {
            User savedUser = userRepository.save(existingUser);
            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Error updating visibility: " + e.getMessage(), e);
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> updateEmailAndPhoneNumber(Long userid, String email, String phoneNumber) {
        Optional<User> getUser = userRepository.findByUserid(userid);
        if (getUser.isPresent()) {
            User existingUser = getUser.get();
            existingUser.setEmail(email);
            existingUser.setPhno(phoneNumber);
            User updatedUser = userRepository.save(existingUser);

            // Wrap the updated user in Optional and return
            return Optional.of(updatedUser);
        }

        // Return empty Optional if user is not found
        return Optional.empty();
    }

    private List<User> getUsersWithBirthdayOn(LocalDate targetDate) {
        List<User> allUsers = userRepository.findAll();

        int targetMonth = targetDate.getMonthValue();
        int targetDay = targetDate.getDayOfMonth();

        return allUsers.stream()
                .filter(user -> user.getBirthday() != null)
                .filter(user -> {
                    LocalDate birthDate = user.getBirthday().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return birthDate.getMonthValue() == targetMonth &&
                            birthDate.getDayOfMonth() == targetDay;
                })
                .toList();
    }

    public List<User> getUsersWithTodayBirthday() {
        return getUsersWithBirthdayOn(LocalDate.now());
    }

    public List<User> getUsersWithTomorrowBirthday() {
        return getUsersWithBirthdayOn(LocalDate.now().plusDays(1));
    }

    public List<User> getUsersWithYesterdayBirthday() {
        return getUsersWithBirthdayOn(LocalDate.now().minusDays(1));
    }


}