package com.system.fsharksocialmedia.controllers.other;


import com.system.fsharksocialmedia.dtos.UserDto;
import com.system.fsharksocialmedia.models.LoginModel;
import com.system.fsharksocialmedia.models.UserModel;
import com.system.fsharksocialmedia.services.other.JwtService;
import com.system.fsharksocialmedia.services.other.UserInfoDetails;
import com.system.fsharksocialmedia.services.other.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;   
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> loadByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userInfoService.getByUsername(username));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> addNewUser(@RequestBody UserModel model) {
        UserDto createdUser = userInfoService.addUser(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserDto> addNewAdmin(@RequestBody UserModel model) {
        UserDto createdUser = userInfoService.addAdmin(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody LoginModel loginModel) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(loginModel.getUsername());
                String roleName = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
                response.put("token", token);
                response.put("role", roleName);
                System.out.println("token: " + token);
                System.out.println("role: " + roleName);
                return ResponseEntity.ok(response);
            } else {
                response.put("token", "");
                response.put("role", "");
                response.put("message", "Đăng nhập thất bại. Sai thông tin tài khoản!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (BadCredentialsException e) {
            response.put("message", "Đăng nhập thất bại. Sai thông tin tài khoản!!!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("message", "Đã xảy ra lỗi trong quá trình xác thực!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
