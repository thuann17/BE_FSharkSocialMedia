package com.system.fsharksocialmedia.services.other;

import com.system.fsharksocialmedia.entities.User;
import com.system.fsharksocialmedia.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Map<String, String> resetTokenStore = new ConcurrentHashMap<>();
    private Map<String, Long> resetTokenExpirationStore = new ConcurrentHashMap<>();

    // Tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*-_";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    // Xử lý yêu cầu quên mật khẩu
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email không tồn tại");
        }
        String newPassword = generateRandomPassword(10);

        try {
            emailService.sendEmail(email, "Yêu cầu đặt lại mật khẩu",
                    "Xin chào, mật khẩu mới của bạn là: " + newPassword + "\nVui lòng đăng nhập tại (http://localhost:3000/login) và thay đổi mật khẩu nếu cần.");
        } catch (Exception e) {
            throw new IllegalStateException("Gửi email không thành công: " + e.getMessage());
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {
        String email = getEmailByToken(token);
        if (email == null || isTokenExpired(email)) {
            throw new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email không tồn tại");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokenStore.remove(email);
        resetTokenExpirationStore.remove(email);
    }

    private String getEmailByToken(String token) {
        return resetTokenStore.entrySet().stream()
                .filter(entry -> entry.getValue().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private boolean isTokenExpired(String email) {
        Long expirationTime = resetTokenExpirationStore.get(email);
        return expirationTime == null || System.currentTimeMillis() > expirationTime;
    }
}
