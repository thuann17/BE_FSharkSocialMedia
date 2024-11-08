package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.FriendRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
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
            return List.of(); // Trả về danh sách rỗng nếu username null hoặc rỗng
        }

        // Gọi thủ tục SQL để lấy danh sách bạn bè
        List<Object[]> results = friendRepository.findFriendNamesByUsername(username);

        // Chuyển đổi kết quả thành danh sách FriendDto
        return results.stream().map(result -> {
            FriendDto friendDto = new FriendDto();
            friendDto.setId((Integer) result[0]); // ID từ kết quả
            // Chuyển đổi các thuộc tính khác từ kết quả Object[]
            friendDto.setUserTarget(userService.toDto(userRepository.findByUsername((String) result[1]).orElse(null))); // Lấy UserDto từ User
            friendDto.setUserSrc(userService.toDto(userRepository.findByUsername((String) result[2]).orElse(null))); // Lấy UserDto từ User
            // Convert Timestamp to Instant
            friendDto.setCreatedate(((Timestamp) result[3]).toInstant());
            friendDto.setStatus((Boolean) result[4]); // status
            friendDto.setFriendName((String) result[6]); // friend_name từ CASE trong thủ tục
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

        // Map the results to a list of FriendDto
        return results.stream().map(result -> {
            FriendDto friendDto = new FriendDto();
            friendDto.setId((Integer) result[0]);
            friendDto.setUserTarget(userService.toDto(userRepository.findByUsername((String) result[1]).orElse(null))); // Lấy UserDto từ User
            friendDto.setUserSrc(userService.toDto(userRepository.findByUsername((String) result[2]).orElse(null))); /// Convert to UserDto for target user
            friendDto.setCreatedate(((Timestamp) result[3]).toInstant());
            friendDto.setStatus((Boolean) result[4]);
            friendDto.setFriendName((String) result[6]); // Friend name from the CASE clause
            friendDto.setFriendUserName((String) result[5]);
            return friendDto;
        }).collect(Collectors.toList());
    }

    ///them ban be
    public String addFriend(String username1, String username2) {
        // Retrieve both users by username
        Optional<User> user1Optional = userRepository.findByUsername(username1);
        Optional<User> user2Optional = userRepository.findByUsername(username2);

        if (!user1Optional.isPresent() || !user2Optional.isPresent()) {
            throw new IllegalArgumentException("User IDs cannot be null or invalid");
        }

        User user1 = user1Optional.get();
        User user2 = user2Optional.get();

        // Add friend relationship
        Friend friendRequest = new Friend();
        friendRequest.setUserTarget(user1);
        friendRequest.setUserSrc(user2);
        friendRequest.setStatus(false);
        friendRequest.setCreatedate(Instant.now());

        friendRepository.save(friendRequest);
        return "Friend request sent!";
    }


    // xóa bạn bằng ID
    public void deleteFriendRequest(Integer id) {
        Friend friendrequest = friendRepository.findById(id).orElseThrow(() -> new RuntimeException("Friend request not found")); // Throw exception if not found

        friendRepository.delete(friendrequest); // Delete the friend request
    }

    //lấy bạn chung
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMutualFriends(String username1, String username2) {
        List<Object[]> results = friendRepository.findMutualFriends(username1, username2);
        List<Map<String, Object>> mutualFriends = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> friendData = new HashMap<>();
            friendData.put("mutualFriend", result[0]);  // Username of mutual friend
            friendData.put("mutualFriendCount", result[1]); // Count (same for each friend)
            mutualFriends.add(friendData);
        }

        return mutualFriends;
    }


}