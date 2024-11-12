package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.FriendRepository;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.FriendService;
import com.system.fsharksocialmedia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/friendrequests")
public class FriendController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired // Make sure to autowire UserService
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @GetMapping("/{username}")
    public ResponseEntity<List<FriendDto>> getFriendsByUserTarget(@PathVariable String username) {
        List<FriendDto> friends = friendService.getFriendsByUserTarget(username);
        if (friends.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no friends found
        }
        return ResponseEntity.ok(friends); // 200 OK with the list of friends
    }

    // cập nhaạt trạng thái
    @PutMapping("/{id}/update-status")
    public ResponseEntity<FriendDto> updateFriendStatus(@PathVariable Integer id) {
        // Call the service to update the friend's status
        FriendDto updatedFriendDto = friendService.updateFriendStatus(id);

        // If no friend found (status could not be updated), return 404 Not Found
        if (updatedFriendDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Return the updated Friend DTO with 200 OK
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
    @PostMapping("/add/{user1}/{user2}")
    public ResponseEntity<String> addFriend(@PathVariable String user1, @PathVariable String user2) {
        try {
            // Call the service to add the friend
            String message = friendService.addFriend(user1, user2);
            return ResponseEntity.ok(message);  // Respond with 200 OK and success message
        } catch (IllegalArgumentException e) {
            // Handle invalid username or duplicate request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
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
    public ResponseEntity<List<User>> getUsersWithoutFriends(@PathVariable("username") String username) {
        List<User> usersWithoutFriends = friendService.getUsersWithoutFriends(username);
        return ResponseEntity.ok(usersWithoutFriends);
    }
}
