package com.calculator.service.impl;

import com.calculator.persistence.entity.UserEntity;
import com.calculator.persistence.repository.IUserRepository;
import com.calculator.presentation.dto.login.AuthLoginRequest;
import com.calculator.presentation.dto.login.AuthLoginResponse;
import com.calculator.presentation.dto.register.AuthRegisterRequest;
import com.calculator.presentation.dto.register.AuthRegisterResponse;
import com.calculator.util.JwtUtils;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    UUID userId = UUID.randomUUID();
    String username = "juan";
    String password = "1234";
    String encodedPassword = "$2a$10$password$10";
    String email = "juan@gmail.com";
    String token = "jwt-token";
    boolean isEnabled = true;
    boolean accountNoExpired = true;
    boolean accountNoLocked = true;
    boolean credentialNoExpired = true;


    @Test
    @DisplayName("Should return the authenticated user data")
    void loadByUsernameTest(){
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetailServiceImpl.CustomUserDetails userDetails =
                new UserDetailServiceImpl.CustomUserDetails(username, password, email,
                        isEnabled, accountNoExpired, credentialNoExpired, accountNoLocked, authorities);

        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(email, userDetails.getEmail());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertEquals(authorities, userDetails.getAuthorities());
    }

    @Test
    @DisplayName("Should search for user by name")
    void loadUserByUsernameTest(){
        UserEntity user = new UserEntity(
                userId,
                username,
                password,
                email,
                LocalDateTime.now(),
                isEnabled,
                accountNoExpired,
                accountNoLocked,
                credentialNoExpired
        );

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals("juan", userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        verify(userRepository).findUserEntityByUsername(username);
    }

    @Test
    @DisplayName("Should fall into error UsernameNotFoundException")
    void loadErrorUserByUsernameTest(){
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername(username));
        assertEquals("The user " + username + " doesn't exist", exception.getMessage());
        verify(userRepository).findUserEntityByUsername(username);
    }

    @Test
    @DisplayName("Should authenticate the user")
    void authenticateTest(){
        UserEntity user = new UserEntity(
                userId,
                username,
                encodedPassword,
                email,
                LocalDateTime.now(),
                true,
                true,
                true,
                true
        );

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        Authentication authentication = userDetailService.authenticate(username, password);

        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());
        assertEquals(encodedPassword, authentication.getCredentials());
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        verify(userRepository).findUserEntityByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when password does not match")
    void authenticateErrorByCredentialsTest(){
        UserEntity user = new UserEntity(
                userId,
                username,
                encodedPassword,
                email,
                LocalDateTime.now(),
                true,
                true,
                true,
                true
        );

        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> userDetailService.authenticate(username, password));
        assertEquals("Invalid password", exception.getMessage());
        verify(userRepository).findUserEntityByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    @DisplayName("Should log in a user")
    void loginUserTest(){
        AuthLoginRequest request = new AuthLoginRequest(username, password);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);

        UserDetailServiceImpl spyService = spy(userDetailService);
        doReturn(authentication).when(spyService).authenticate(username, password);

        when(jwtUtils.createToken(any(Authentication.class))).thenReturn(token);

        AuthLoginResponse response = spyService.loginUser(request);

        assertNotNull(response);
        assertEquals("User logged in correctly", response.message());
        assertEquals(username, response.username());
        verify(jwtUtils).createToken(any(Authentication.class));
    }

    @Test
    @DisplayName("Should register a user")
    void registerUserTest(){
        AuthRegisterRequest request = new AuthRegisterRequest(username, password, email);

        UserEntity user = new UserEntity(
                userId,
                username,
                encodedPassword,
                email,
                LocalDateTime.now(),
                true,
                true,
                true,
                true
        );

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        AuthRegisterResponse response = userDetailService.registerUser(request);

        assertNotNull(response);
        assertEquals("User created successfully", response.message());
        assertEquals(username, response.username());
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get the authenticated username")
    void getCurrentUsernameTest(){
        Authentication authentication = mock(Authentication.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email("juan@gmail.com")
                .build();

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(user));

        UserEntity userEntity = userDetailService.getCurrentUsername();

        assertNotNull(userEntity);
        assertEquals(username, userEntity.getUsername());
        verify(authentication).getName();
        verify(securityContext).getAuthentication();
        verify(userRepository).findUserEntityByUsername(username);
    }

    @Test
    @DisplayName("Should throw AuthenticationCredentialsNotFoundException when the username is not authenticated")
    void getCurrentUsernameErrorTest(){
        Authentication authentication = mock(Authentication.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());

        AuthenticationCredentialsNotFoundException exception =
                assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userDetailService.getCurrentUsername());

        assertEquals("User don't authenticated", exception.getMessage());
    }
}
