package com.system.fsharksocialmedia.controllers;

import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAccounts(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "7") int size,
            @RequestParam(required = false) String search) {
        Page<UserDto> accounts = accountService.getUsers(page, size, search);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateAccount(@PathVariable String username, @RequestBody UserModel userModel) {
        return ResponseEntity.ok(accountService.updateAccount(username, userModel));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteAccount(@PathVariable String username) {
        if (userRepository.existsById(username)) {
            accountService.deleteUser(username);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

