package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.FriendRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired // This line ensures userService is injected
    private UserService userService;

    // Convert from entity to DTO
    public FriendDto toDto(Friend friend) {
        if (friend == null) {
            return null;  // Return null if entity is null
        }
        FriendDto dto = new FriendDto();
        dto.setId(friend.getId());
        dto.setUserTarget(userService.toDto(friend.getUserTarget())); // Convert User to UserDto
        dto.setUserSrc(userService.toDto(friend.getUserSrc()));       // Convert User to UserDto
        dto.setCreatedate(friend.getCreatedate());
        dto.setStatus(friend.getStatus());
        return dto;
    }

    // Lấy danh sách bạn bè theo userTarget
    public List<FriendDto> getFriendsByUserTarget(String username) {
        if (username == null || username.isEmpty()) {
            return List.of(); // Return an empty list if username is invalid
        }
        // Call SQL procedure to get the list of friends
        List<Object[]> results = friendRepository.findFriendNamesByUsername(username);

        // Map the results to a list of FriendDto
        return results.stream().map(result -> {
            FriendDto friendDto = new FriendDto();
            friendDto.setId((Integer) result[0]); // ID from the result
            friendDto.setUserTarget(userService.toDto(userRepository.findByUsername((String) result[1]).orElse(null))); // UserDto conversion
            friendDto.setUserSrc(userService.toDto(userRepository.findByUsername((String) result[2]).orElse(null))); // UserDto conversion
            // Check if result[3] (createdate) is null before converting
            Timestamp timestamp = (Timestamp) result[3];
            if (timestamp != null) {
                friendDto.setCreatedate(timestamp.toInstant()); // Convert Timestamp to Instant
            }
            friendDto.setStatus((Boolean) result[4]); // status
            friendDto.setFriendName((String) result[6]); // friend_name from CASE
            friendDto.setFriendUserName((String) result[5]);
            return friendDto;
        }).collect(Collectors.toList());
    }


    // Lấy danh sách người follow theo userTarget
    public List<FriendDto> getFollowers(String username) {
        if (username == null || username.isEmpty()) {
            return List.of(); // Return empty list if username is invalid
        }

        // Call repository to fetch followers based on the stored procedure
        List<Object[]> results = friendRepository.findFollowersByUsername(username);

        // Map the results to a list of FriendDto with null checks
        return results.stream().map(result -> {
            FriendDto friendDto = new FriendDto();

            friendDto.setId((Integer) result[0]);

            // Convert userTarget to UserDto, handling potential null
            String userTargetUsername = (String) result[1];
            User userTarget = userRepository.findByUsername(userTargetUsername).orElse(null);
            friendDto.setUserTarget(userTarget != null ? userService.toDto(userTarget) : null);

            // Convert userSrc to UserDto, handling potential null
            String userSrcUsername = (String) result[2];
            User userSrc = userRepository.findByUsername(userSrcUsername).orElse(null);
            friendDto.setUserSrc(userSrc != null ? userService.toDto(userSrc) : null);

            friendDto.setCreatedate(((Timestamp) result[3]).toInstant());
            friendDto.setStatus((Boolean) result[4]);

            // Additional fields with appropriate casting
            friendDto.setFriendUserName((String) result[5]);
            friendDto.setFriendName((String) result[6]);

            return friendDto;
        }).collect(Collectors.toList());
    }


    //lây goi y ket ban
    @Transactional(readOnly = true)
    public List<User> getUsersWithoutFriends(String username) {
        return userRepository.findUsersWithoutFriends(username);
    }


    // Method to add a friend
    public String addFriend(String username1, String username2) {
        // Retrieve both users by username
        Optional<User> user1Optional = userRepository.findByUsername(username1);
        Optional<User> user2Optional = userRepository.findByUsername(username2);

        if (!user1Optional.isPresent() || !user2Optional.isPresent()) {
            throw new IllegalArgumentException("User IDs cannot be null or invalid");
        }

        User user1 = user1Optional.get();
        User user2 = user2Optional.get();

        // Check if the users are already friends or have sent a friend request to avoid duplicate requests
        if (friendRepository.existsByUserSrcAndUserTarget(user1, user2)) {
            throw new IllegalArgumentException("Friend request already sent or users are already friends.");
        }

        // Create a new friend request
        Friend friendRequest = new Friend();
        friendRequest.setUserTarget(user1);
        friendRequest.setUserSrc(user2);
        friendRequest.setStatus(false);  // Friend request is pending
        friendRequest.setCreatedate(Instant.now());

        // Save the friend request in the repository
        friendRepository.save(friendRequest);

        return "Friend request sent successfully!";
    }

    //chấp nhận yê cầu kết bạn
    public FriendDto updateFriendStatus(Integer friendId) {
        // Find the Friend entity by its ID
        Friend friend = friendRepository.findById(friendId).orElse(null);

        // If the Friend entity doesn't exist, return null or handle it accordingly
        if (friend == null) {
            return null;
        }

        // Check if the current status is false, then update to true
        if (Boolean.FALSE.equals(friend.getStatus())) {
            friend.setStatus(true); // Set the status to true
            friend = friendRepository.save(friend); // Save the updated friend entity to the database
        }

        // Convert the updated Friend entity to a DTO and return it
        return toDto(friend); // Assuming `toDto` is a method that converts Friend to FriendDto
    }

    // Method to delete a friend by ID
    public void deleteFriendRequest(Integer id) {
        Friend friendrequest = friendRepository.findById(id).orElseThrow(() -> new RuntimeException("Friend request not found")); // Throw exception if not found

        friendRepository.delete(friendrequest); // Delete the friend request
    }

}