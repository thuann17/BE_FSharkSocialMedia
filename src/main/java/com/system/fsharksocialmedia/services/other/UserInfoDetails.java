package com.system.fsharksocialmedia.services.other;

import com.system.fsharksocialmedia.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private String name;
    private String password;
    private List<GrantedAuthority> authorities;
    private Integer roleId;
    private Boolean isActive;  // Store active status

    public UserInfoDetails(User user) {
        this.name = user.getUsername();
        this.password = user.getPassword();
        this.roleId = user.getRoles().getId();
        this.authorities = user.getRoles() != null ?
                List.of(new SimpleGrantedAuthority(user.getRoles().getRole())) :
                List.of(); // Ensure authorities are based on role
        this.isActive = user.getActive();  // Get account active status
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // You can implement expiration logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive != null && isActive;  // Account is locked if 'active' is false
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // You can implement expiration logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true;  // You can implement enabled logic if needed
    }
}
