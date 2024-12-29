package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.services.admin.UserService;
import com.system.fsharksocialmedia.services.user.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/friend-requests")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<List<FriendDto>> getFriendsByUserTarget(@PathVariable String username) {
        List<FriendDto> friends = friendService.getFriendsByUserTarget(username);
        if (friends.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(friends);
    }

    // cập nhaạt trạng thái
    @PutMapping("/{id}/update-status")
    public ResponseEntity<FriendDto> updateFriendStatus(@PathVariable Integer id) {
        FriendDto updatedFriendDto = friendService.updateFriendStatus(id);
        if (updatedFriendDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedFriendDto);
    }

    //lay ds follower
    @GetMapping("/followers")
    public ResponseEntity<List<FriendDto>> getFollowers(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Handle invalid input early
        }
        try {
            List<FriendDto> followers = friendService.getFollowers(username);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //them bạn bè
    @PostMapping("/add-friend/{user1}/{user2}")
    public ResponseEntity<String> addFriend(@PathVariable String user1, @PathVariable String user2) {
        try {
            // Call the service to add the friend
            String message = friendService.addFriend(user1, user2);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding friend: " + e.getMessage());
        }
    }
    //xóa bạn bè
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriendRequest(@PathVariable Integer id) {
        try {
            friendService.deleteFriendRequest(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

    //láy danh sách gơị ý kb
    @GetMapping("/without-friends/{username}")
    public ResponseEntity<List<UserDto>> getUsersWithoutFriends(@PathVariable("username") String username) {
        List<User> usersWithoutFriends = friendService.getUsersWithoutFriends(username);

        // Map User entities to UserDto
        List<UserDto> userDtos = usersWithoutFriends.stream()
                .map(user -> userService.toDto(user)) // Use a mapping method
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }
}
