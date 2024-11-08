package com.system.fsharksocialmedia.services;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.Friend;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.FriendRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Lây danh sách bạn bè
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
            friendDto.setCreatedate((Instant) result[3]); // createdate
            friendDto.setStatus((Boolean) result[4]); // status
            friendDto.setFriendName((String) result[5]); // friend_name từ CASE trong thủ tục
            return friendDto;
        }).collect(Collectors.toList());
    }



    //lấy danh sach nguoi follow
//    public List<FriendDto> getFollowByUserSrc(Optional<User> userSrc) {
//        // Check if userSrc is present
//        if (userSrc.isEmpty()) {
//            return List.of(); // Return empty list if userSrc is not present
//        }
//
//        // Retrieve friend requests for the given userSrc
//        List<Friend> friendRequests = friendRepository.findByUserSrcAndStatusFollow(userSrc.get());
//
//        // Convert to DTOs
//        return friendRequests.stream().map(this::toDto) // Call the method to convert to DTO
//                .collect(Collectors.toList());
//    }

//    public FriendDto toggleFriendrequestStatus(Integer requestId) {
//        // Fetch the friend request by ID
//        Friend friend = friendRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Friend request not found"));
//
//        // Check current status and toggle between 1 and 2
//        if (friend.getStatus() == true) {
//            // Change to status 2
//
//            friend.setStatus(false);
//        } else if (friend.getStatus() == false) {
//            // Change to status 1
//
//            friend.setStatus(true);
//        }
//
//        // Save the updated friend request
//        Friend updatedRequest = friendRepository.save(friend);
//
//        // Convert the updated friend request to DTO and return it
//        return toDto(updatedRequest);
//    }

    // Method to delete a friend by ID
    public void deleteFriendRequest(Integer id) {
        Friend friendrequest = friendRepository.findById(id).orElseThrow(() -> new RuntimeException("Friend request not found")); // Throw exception if not found

        friendRepository.delete(friendrequest); // Delete the friend request
    }

}