package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user/account")
public class UserAccountController {
    @Autowired
    AccountService useraccountService;
    @Autowired
    private UserRepository userRepository;


    //lấy danh sách tài khoan
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAccounts(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "7") int size,
            @RequestParam(required = false) String search) {
        Page<UserDto> accounts = useraccountService.getUsers(page, size, search);
        return ResponseEntity.ok(accounts);
    }

    //cập nhật toong tin tk
    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateAccount(@PathVariable String username, @RequestBody UserModel userModel) {
        return ResponseEntity.ok(useraccountService.updateAccount(username, userModel));
    }

    //xóa tai khoản
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteAccount(@PathVariable String username) {
        if (userRepository.existsById(username)) {
            useraccountService.deleteUser(username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //lay tai khoan theo username
    @GetMapping("/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> userDto = useraccountService.findByUsername(username);

        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }

}

