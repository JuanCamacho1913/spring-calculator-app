package com.calculator.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.calculator.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidatorTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private DecodedJWT decodedJWT;

    private JwtTokenValidator jwtTokenValidator;

    @BeforeEach
    void setUp() {
        jwtTokenValidator = new JwtTokenValidator(jwtUtils);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalTest() throws ServletException, IOException {
        String token = "Bearer valid.token.here";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtils.validationToken("valid.token.here")).thenReturn(decodedJWT);
        when(jwtUtils.extractUsername(decodedJWT)).thenReturn("user");

        jwtTokenValidator.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("user", auth.getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalWithNoTokenTest() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtTokenValidator.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validationToken(any());
    }

    @Test
    void doFilterInternalWithInvalidPrefixTest() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidPrefix token");

        jwtTokenValidator.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validationToken(any());
    }
}
