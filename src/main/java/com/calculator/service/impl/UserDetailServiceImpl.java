package com.calculator.service.impl;

import com.calculator.persistence.entity.UserEntity;
import com.calculator.persistence.repository.IUserRepository;
import com.calculator.presentation.dto.login.AuthLoginRequest;
import com.calculator.presentation.dto.register.AuthRegisterResponse;
import com.calculator.presentation.dto.register.AuthRegisterRequest;
import com.calculator.presentation.dto.login.AuthLoginResponse;
import com.calculator.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    public static class CustomUserDetails implements UserDetails {
        private final String username;
        private final String password;
        private final String email;
        private final boolean enabled;
        private final boolean accountNonExpired;
        private final boolean credentialsNonExpired;
        private final boolean accountNonLocked;
        private final Collection<? extends GrantedAuthority> authorities;

        public CustomUserDetails(String username, String password, String email,
                                 boolean enabled, boolean accountNonExpired,
                                 boolean credentialsNonExpired, boolean accountNonLocked,
                                 Collection<? extends GrantedAuthority> authorities) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.enabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.authorities = authorities;
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
            return username;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return accountNonExpired;
        }

        @Override
        public boolean isAccountNonLocked() {
            return accountNonLocked;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return credentialsNonExpired;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user " + username + " doesn't exist"));

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isCredentialNoExpired(),
                user.isAccountNoLocked(),
                new ArrayList<>()
        );
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    public AuthLoginResponse loginUser(AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);

        String accessToken = this.jwtUtils.createToken(authentication);

        return new AuthLoginResponse(username, "User logged in correctly", accessToken, true);
    }

    public AuthRegisterResponse registerUser(AuthRegisterRequest authregisterRequest){
        String username = authregisterRequest.username();
        String password = authregisterRequest.password();
        String email = authregisterRequest.email();

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userEntitySaved = this.userRepository.save(userEntity);

        return new AuthRegisterResponse(userEntitySaved.getUsername(),userEntitySaved.getEmail(),"User created successfully",true);
    }

    public UserEntity getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return this.userRepository.findUserEntityByUsername(username).orElseThrow(() ->
                new AuthenticationCredentialsNotFoundException("User don't authenticated"));
    }

}
