package com.sadetech.friend_request_module.Service;

import com.sadetech.friend_request_module.Dto.UserDTO;
import com.sadetech.friend_request_module.FeignConnect.UserFeignClient;
import com.sadetech.friend_request_module.Model.FriendRequest;
import com.sadetech.friend_request_module.Model.Status;
import com.sadetech.friend_request_module.Repository.FriendRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(FriendRequestService.class);

    public FriendRequest makeRequest(Long senderId, Long recipientId) {
        // Fetch sender and recipient users by their IDs
        UserDTO sender = getUserById(senderId, "Sender user not found.");
        UserDTO recipient = getUserById(recipientId, "Recipient user not found.");

        // Prevent a user from sending a friend request to themselves
        if (senderId.equals(recipientId)) {
            throw new IllegalArgumentException("User cannot send a friend request to themselves.");
        }

        // Check if a friend request already exists between the two users
        Optional<FriendRequest> existingRequest = friendRequestRepository.findBySenderIdAndRecipientId(senderId,recipientId);
        // If the request already exists
        if (existingRequest.isPresent()) {
            FriendRequest request = existingRequest.get();

            // If the request was declined, allow resending by setting status back to PENDING
            if (request.getStatus() == Status.DECLINED) {
                logger.info("Resending friend request from {} to {} by setting status to PENDING.", senderId, recipientId);
                request.setStatus(Status.PENDING);

                // Send notification for the resent request
                UserDTO recipientUser = userFeignClient.getUserById(recipientId);
                String recipientEmail = recipientUser.getEmail();  // Get the email
                String senderName = sender.getName(); // Get the sender's name
                String notificationMessage = "You have a new friend request from " + senderName + ".";
                String profileImagePath = sender.getProfileImagePath();
                notificationService.createNotification(recipientId, notificationMessage, "FRIEND_PENDING", recipientEmail,profileImagePath);

                return friendRequestRepository.save(request);  // Update and save the status back to PENDING
            }

            // If the request is already pending or accepted, throw an error
            if (request.getStatus() == Status.PENDING) {
                throw new IllegalArgumentException("Friend request is already pending.");
            } else if (request.getStatus() == Status.ACCEPTED) {
                throw new IllegalArgumentException("Friend request has already been accepted.");
            }
        }

        // Create a new friend request if no prior request exists
        FriendRequest newRequest = new FriendRequest();
        newRequest.setSenderId(senderId);
        newRequest.setRecipientId(recipientId);
        newRequest.setStatus(Status.PENDING);

        logger.info("Friend request sent successfully from {} to {}.", senderId, recipientId);

        UserDTO recipientUser = userFeignClient.getUserById(recipientId);
        String recipientEmail = recipientUser.getEmail();  // Get the email
        String senderName = sender.getName(); // Get the sender's name
        String profileImagePath =  sender.getProfileImagePath();
        // Create a notification after saving the new request
        String notificationMessage = "You have a new friend request from " + senderName + ".";
        notificationService.createNotification(recipientId, notificationMessage, "FRIEND_PENDING", recipientEmail,profileImagePath);

        return friendRequestRepository.save(newRequest);
    }


    public void acceptFriendRequest(Long senderId, Long recipientId) {
        // Find the friend request by sender and recipient
        FriendRequest request = friendRequestRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found."));

        // Check if the request is already accepted or declined to prevent duplication
        if (request.getStatus() == Status.ACCEPTED) {
            throw new IllegalArgumentException("Friend request has already been accepted.");
        }

        if (request.getStatus() == Status.DECLINED) {
            throw new IllegalArgumentException("Friend request has already been declined.");
        }

        // Update the status to ACCEPTED
        request.setStatus(Status.ACCEPTED);
        logger.info("Friend request has been accepted by {}.",recipientId);

        UserDTO senderUser = userFeignClient.getUserById(senderId);
        String senderEmail = senderUser.getEmail();  // Get the email
        String recipientName = userFeignClient.getUserById(recipientId).getName(); // Get recipient's name
        String profileImagePath = userFeignClient.getUserById(recipientId).getProfileImagePath();

        // Create a notification after accepting the request
        String notificationMessage = recipientName + " has accepted your friend request.";
        notificationService.createNotification(senderId, notificationMessage, "FRIEND_ACCEPTED",senderEmail,profileImagePath);

        friendRequestRepository.save(request);

    }


    // Decline a friend request
    public void declineFriendRequest(Long senderId, Long recipientId) {
        FriendRequest request = friendRequestRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found."));

        if(request.getStatus() == Status.DECLINED){
            throw new IllegalArgumentException("Can't decline user multiple times.");
        }
        if(request.getStatus() == Status.ACCEPTED){
            throw new IllegalArgumentException("Can't decline accepted request.");
        }else {
            // Update the request status to DECLINED
            request.setStatus(Status.DECLINED);
        }
        logger.info("Friend request declined by {}",recipientId);

        UserDTO senderUser = userFeignClient.getUserById(senderId);
        String senderEmail = senderUser.getEmail();  // Get the email
        String recipientName = userFeignClient.getUserById(recipientId).getName(); // Get recipient's name

        // Create a notification after declining the request
        String notificationMessage = recipientName + " has declined your friend request.";
        notificationService.createNotification(senderId, notificationMessage, "FRIEND_DECLINED",senderEmail,null);

        friendRequestRepository.save(request);
    }

    // Optional: Method to create a friendship after the request is accepted
    private void createFriendship(Long senderId, Long recipientId) {
        // Logic to create a friendship in the friendship repository
        FriendRequest friendship = new FriendRequest();
        friendship.setSenderId(senderId);
        friendship.setRecipientId(recipientId);
        friendship.setRequestTime(LocalDateTime.now());
        // Save the friendship entity (FriendshipRepository.save(friendship))
        friendRequestRepository.save(friendship);
    }

    public void deleteFriend(Long senderId, Long recipientId) {
        // Check for a friendship where the user is either the sender or the recipient
        Optional<FriendRequest> friendRequest = friendRequestRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .or(() -> friendRequestRepository.findBySenderIdAndRecipientId(recipientId, senderId));

        // If a friendship exists, delete it; otherwise, throw an exception
        if (friendRequest.isPresent()) {
            logger.info("User {} has removed you from friend list",senderId);
            friendRequestRepository.delete(friendRequest.get());
        } else {
            throw new IllegalArgumentException("Friendship relation does not exist.");
        }
    }


    public Map<String, Object> getFriendListAndCount(Long userId) {
        // Get all accepted friend requests where the user is either the sender or the recipient
        List<FriendRequest> friendRequestsAsSender = friendRequestRepository.findAllBySenderIdAndStatus(userId, Status.ACCEPTED);
        List<FriendRequest> friendRequestsAsRecipient = friendRequestRepository.findAllByRecipientIdAndStatus(userId, Status.ACCEPTED);

        List<UserDTO> friends = new ArrayList<>();
        Set<Long> uniqueUserIds = new HashSet<>();  // Set to track unique user IDs

        // Get friends where the user is the sender
        for (FriendRequest request : friendRequestsAsSender) {
            Long recipientId = request.getRecipientId();
            if (!uniqueUserIds.contains(recipientId)) {
                UserDTO recipient = getUserById(recipientId, "Friend not found.");
                logger.info("The user DTO contains id {} for recipient",recipient.getId() );
                recipient.setRequestTime(request.getRequestTime());
                friends.add(recipient);
                uniqueUserIds.add(recipientId);  // Add the user ID to the set
            }
        }

        // Get friends where the user is the recipient
        for (FriendRequest request : friendRequestsAsRecipient) {
            Long senderId = request.getSenderId();
            if (!uniqueUserIds.contains(senderId)) {
                UserDTO sender = getUserById(senderId, "Friend not found.");
                logger.info("The user DTO contains id {} for sender",sender.getId());
                sender.setRequestTime(request.getRequestTime());
                friends.add(sender);
                uniqueUserIds.add(senderId);  // Add the user ID to the set
            }
        }

        // Create a Map to hold the response data
        Map<String, Object> response = new HashMap<>();
        response.put("userId",userId);
        response.put("friendCount", friends.size());
        response.put("friends", friends);
        logger.info("Got the Friend List of user {}", userId);

        return response;
    }


    // Get the list of pending requests sent to the user with their details
    public Map<String, Object> getPendingRequestsDetails(Long userId) {
        // Get all pending friend requests where the user is the recipient
        List<FriendRequest> pendingRequests = friendRequestRepository.findAllByRecipientIdAndStatus(userId, Status.PENDING);

        List<Map<String, Object>> pendingRequestsList = new ArrayList<>();

        // Prepare pending request details
        for (FriendRequest request : pendingRequests) {
            UserDTO sender = getUserById(request.getSenderId(), "Sender not found.");
            Map<String, Object> requestDetails = new HashMap<>();
            requestDetails.put("senderId", sender.getId());
            requestDetails.put("senderName", sender.getName());
            requestDetails.put("senderImagePath",sender.getProfileImagePath());
            requestDetails.put("senderBannerPath", sender.getBannerImagePath());
            requestDetails.put("requestTime",request.getRequestTime());
            pendingRequestsList.add(requestDetails);
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("pendingCount", pendingRequests.size());
        response.put("pendingRequests", pendingRequestsList);

        logger.info("Got the Pending request list of user {}", userId);

        return response;
    }


    // Get the number of pending friend requests sent by the user
    public Map<String, Object> getSentRequestsDetails(Long senderId) {
        // Get all pending friend requests sent by the user
        List<FriendRequest> sentRequests = friendRequestRepository.findAllBySenderIdAndStatus(senderId, Status.PENDING);

        List<Map<String, Object>> sentRequestsList = new ArrayList<>();

        // Prepare sent request details
        for (FriendRequest request : sentRequests) {
            UserDTO recipient = getUserById(request.getRecipientId(), "Recipient not found.");
            Map<String, Object> requestDetails = new HashMap<>();
            requestDetails.put("recipientId", recipient.getId());
            requestDetails.put("recipientName", recipient.getName());
            requestDetails.put("recipientImagePath",recipient.getProfileImagePath());
            requestDetails.put("recipientBannerPath",recipient.getBannerImagePath());
            requestDetails.put("requestTime",request.getRequestTime());
            sentRequestsList.add(requestDetails);
        }

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("sentCount", sentRequests.size());
        response.put("sentRequests", sentRequestsList);

        logger.info("Got the request sent list of user {}", senderId);

        return response;
    }


    public List<UserDTO> getMutualFriends(Long userId1, Long userId2) {
        // Get friends of the first user
        List<FriendRequest> friendsOfUser1AsSender = friendRequestRepository.findAllBySenderIdAndStatus(userId1, Status.ACCEPTED);
        List<FriendRequest> friendsOfUser1AsRecipient = friendRequestRepository.findAllByRecipientIdAndStatus(userId1, Status.ACCEPTED);

        // Get friends of the second user
        List<FriendRequest> friendsOfUser2AsSender = friendRequestRepository.findAllBySenderIdAndStatus(userId2, Status.ACCEPTED);
        List<FriendRequest> friendsOfUser2AsRecipient = friendRequestRepository.findAllByRecipientIdAndStatus(userId2, Status.ACCEPTED);

        // Collect friend IDs
        Set<Long> friendsOfUser1 = new HashSet<>();
        friendsOfUser1.addAll(friendsOfUser1AsSender.stream().map(FriendRequest::getRecipientId).toList());
        friendsOfUser1.addAll(friendsOfUser1AsRecipient.stream().map(FriendRequest::getSenderId).toList());

        Set<Long> friendsOfUser2 = new HashSet<>();
        friendsOfUser2.addAll(friendsOfUser2AsSender.stream().map(FriendRequest::getRecipientId).toList());
        friendsOfUser2.addAll(friendsOfUser2AsRecipient.stream().map(FriendRequest::getSenderId).toList());

        // Find intersection
        friendsOfUser1.retainAll(friendsOfUser2);

        // Get mutual friends details
        List<UserDTO> mutualFriends = new ArrayList<>();
        for (Long friendId : friendsOfUser1) {
            UserDTO user = getUserById(friendId, "User not found.");
            mutualFriends.add(user);
        }

        logger.info("Got the mutual friend list of {} and {}",userId1,userId2);

        return mutualFriends;
    }


    private UserDTO getUserById(Long userId, String errorMessage) {

            UserDTO user = userFeignClient.getUserById(userId);  // Fetch user by ID from Feign client
            if (user == null) {
                throw new IllegalArgumentException("User Not Found"); // Throw custom error message if user not found
            }
            return user;
    }

    public Map<String, Object> getFriendListByVisibility(Long userId, Long requestedUserId) {
        UserDTO userDTO = getUserById(userId, "User not found");

        // Get the visibility setting
        String visibility = userDTO.getVisibility();

        // Handle PRIVATE visibility: Only the owner can see their friend list
        if (visibility.equals("PRIVATE") && !userId.equals(requestedUserId)) {
            logger.error("Friend list is private. User ID {} does not match requested user ID {}.", userId, requestedUserId);
            throw new IllegalArgumentException("Friend list is private and cannot be accessed by other users.");
        }

        // Handle FRIENDS_ONLY visibility: Only the owner and their friends can see the friend list
        if (visibility.equals("FRIENDS_ONLY")) {
            // If the requested user is the owner, allow access
            if (userId.equals(requestedUserId)) {
                logger.info("Owner is accessing his own friend list.");
            } else {
                // Check if requestedUserId is a friend of userId
                if (!isFriend(userId, requestedUserId)) {
                    logger.error("Friend list is restricted to friends. User ID {} is not a friend of requested user ID {}.", requestedUserId, userId);
                    throw new IllegalArgumentException("You cannot access the friend list because you are not a friend.");
                }
            }
        }

        // Handle PUBLIC visibility: Anyone can see the friend list
        if (!visibility.equals("PUBLIC") && !visibility.equals("FRIENDS_ONLY") && !visibility.equals("PRIVATE")) {
            logger.info("Friend list is visible to everyone as it is set to PUBLIC.");
        }

        // Fetch and return the friend list if the user passes the visibility checks
        return getFriendListAndCount(userId);
    }

    private boolean isFriend(Long userId, Long friendId) {
        // Logic to check friendship in your database
        // This could be a repository call or a service method that verifies the friendship status
        Optional<FriendRequest> friendRequest = friendRequestRepository.findBySenderIdAndRecipientId(userId, friendId);
        return friendRequest.isPresent() && friendRequest.get().getStatus() == Status.ACCEPTED;
    }

}