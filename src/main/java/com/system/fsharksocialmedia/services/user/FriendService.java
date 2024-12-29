package com.system.fsharksocialmedia.services.user;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.FriendRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.admin.UserService;
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
        List<Object[]> results = friendRepository.findFriendNamesByUsername(username);

        // Map the results to a list of FriendDto
        return results.stream().map(result -> {
            FriendDto friendDto = new FriendDto();
            friendDto.setId((Integer) result[0]); // ID from the result
            friendDto.setUserTarget(userService.toDto(userRepository.findById((String) result[1]).orElse(null))); // UserDto conversion
            friendDto.setUserSrc(userService.toDto(userRepository.findById((String) result[2]).orElse(null))); // UserDto conversion
            // Check if result[3] (createdate) is null before converting
            Timestamp timestamp = (Timestamp) result[3];
            if (timestamp != null) {
                friendDto.setCreatedate(timestamp.toInstant()); // Convert Timestamp to Instant
            }
            friendDto.setStatus((Boolean) result[4]); // status
            friendDto.setFriendUserName((String) result[5]);
            friendDto.setFriendName((String) result[6]);
            friendDto.setFriendAvatar((String) result[7]);
            return friendDto;
        }).collect(Collectors.toList());
    }


    // Lấy danh sách người follow theo userTarget
    public List<FriendDto> getFollowers(String username) {
        if (username == null || username.isEmpty()) {
            return List.of();
        }
        List<Object[]> results = friendRepository.findFollowersByUsername(username);
        return results.stream().map(result -> {
            FriendDto friendDto = new FriendDto();

            friendDto.setId((Integer) result[0]);

            // Convert userTarget to UserDto, handling potential null
            String userTargetUsername = (String) result[1];
            User userTarget = userRepository.findById(userTargetUsername).orElse(null);
            friendDto.setUserTarget(userTarget != null ? userService.toDto(userTarget) : null);
            String userSrcUsername = (String) result[2];
            User userSrc = userRepository.findById(userSrcUsername).orElse(null);
            friendDto.setUserSrc(userSrc != null ? userService.toDto(userSrc) : null);

            friendDto.setCreatedate(((Timestamp) result[3]).toInstant());
            friendDto.setStatus((Boolean) result[4]);
            friendDto.setFriendUserName((String) result[5]);
            friendDto.setFriendName((String) result[6]);
            friendDto.setFriendAvatar((String) result[7]);
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
        Optional<User> user1Optional = userRepository.findById(username1);
        Optional<User> user2Optional = userRepository.findById(username2);
        if (!user1Optional.isPresent() || !user2Optional.isPresent()) {
            throw new IllegalArgumentException("User IDs cannot be null or invalid");
        }
        User user1 = user1Optional.get();
        User user2 = user2Optional.get();

        if (friendRepository.existsByUserSrcAndUserTarget(user1, user2)) {
            throw new IllegalArgumentException("Friend request already sent or users are already friends.");
        }

        Friend friendRequest = new Friend();
        friendRequest.setUserTarget(user1);
        friendRequest.setUserSrc(user2);
        friendRequest.setStatus(false);
        friendRequest.setCreatedate(Instant.now());
        friendRepository.save(friendRequest);
        return "Friend request sent successfully!";
    }

    //chấp nhận yê cầu kết bạn
    public FriendDto updateFriendStatus(Integer friendId) {
        Friend friend = friendRepository.findById(friendId).orElse(null);
        if (friend == null) {
            return null;
        }
        if (Boolean.FALSE.equals(friend.getStatus())) {
            friend.setStatus(true);
            friend = friendRepository.save(friend);
        }
        return toDto(friend);
    }

    // Method to delete a friend by ID
    public void deleteFriendRequest(Integer id) {
        Friend friendrequest = friendRepository.findById(id).orElseThrow(() -> new RuntimeException("Friend request not found"));
        friendRepository.delete(friendrequest);
    }

}