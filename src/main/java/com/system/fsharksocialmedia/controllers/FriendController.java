package com.system.fsharksocialmedia.controllers;

import com.system.fsharksocialmedia.dtos.FriendDto;
import com.system.fsharksocialmedia.entities.Friend;
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
import java.util.Optional;

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

    //lấy danh sách follower
    @GetMapping("/followers")
    public ResponseEntity<List<FriendDto>> getFollowers(@RequestParam String username) {
        try {
            List<FriendDto> followers = friendService.getFollowers(username);
            return ResponseEntity.ok(followers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //them bạn bè
    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String username1, @RequestParam String username2) {
        try {
            String message = friendService.addFriend(username1, username2);
            return ResponseEntity.ok(message);  // Respond with 200 OK and success message
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding friend: " + e.getMessage());
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
        List<User> usersWithoutFriends = userService.getUsersWithoutFriends(username);
        return ResponseEntity.ok(usersWithoutFriends);
    }

}
