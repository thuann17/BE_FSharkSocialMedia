package com.system.fsharksocialmedia.controllers.admin;

import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.models.LoginModel;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.repositories.UserRepository;
import com.system.fsharksocialmedia.services.admin.AccountService;
import com.system.fsharksocialmedia.services.other.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/user")
    public ResponseEntity<Page<UserDto>> getUserAccounts(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "7") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String role) {
        Page<UserDto> accounts = accountService.getUserAccount(page, size, search, active, role);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/admin/{username}")
    public ResponseEntity<Page<UserDto>> getAdminAccounts(
            @PathVariable String username,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "7") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String role
    ) {
        Page<UserDto> accounts = accountService.getAdminAccount(page, size, search, active, role, username);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/lock-unlock")
    public ResponseEntity<Map<String, String>> lockUnlockAccount(@RequestBody LoginModel request) {
        Map<String, String> response = new HashMap<>();
        try {
            UserDto userDto = accountService.lockUnlockAccount(request.getUsername(), request.getPassword());
            response.put("message", "Tài khoản đã được cập nhật thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Có lỗi xảy ra trong quá trình xử lý yêu cầu.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

