package com.calculator.Util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.calculator.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private String privateKey = "mySecretKey";
    private String userGenerator = "calculator-app";

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        // Inyección por reflexión
        Field privateKeyField = JwtUtils.class.getDeclaredField("privateKey");
        privateKeyField.setAccessible(true);
        privateKeyField.set(jwtUtils, privateKey);

        Field userGeneratorField = JwtUtils.class.getDeclaredField("userGenerator");
        userGeneratorField.setAccessible(true);
        userGeneratorField.set(jwtUtils, userGenerator);
    }

    @Test
    void createTokenTest() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Authentication auth = new UsernamePasswordAuthenticationToken("juan", null, List.of(authority));

        String token = jwtUtils.createToken(auth);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validationTokenTest() {
        Authentication auth = new UsernamePasswordAuthenticationToken("maria", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        String token = jwtUtils.createToken(auth);

        DecodedJWT decodedJWT = jwtUtils.validationToken(token);

        assertNotNull(decodedJWT);
        assertEquals("maria", decodedJWT.getSubject());
    }

    @Test
    void extractUsernameTest() {
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getSubject()).thenReturn("usuario");

        String username = jwtUtils.extractUsername(decodedJWT);

        assertEquals("usuario", username);
    }

    @Test
    void getSpecificClaimTest() {
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        Claim claim = mock(Claim.class);

        when(decodedJWT.getClaim("authorities")).thenReturn(claim);

        Claim result = jwtUtils.getSpecificClaim(decodedJWT, "authorities");

        assertEquals(claim, result);
    }

    @Test
    void extractAllClaimsTest() {
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        Map<String, Claim> claims = new HashMap<>();
        claims.put("authorities", mock(Claim.class));

        when(decodedJWT.getClaims()).thenReturn(claims);

        Map<String, Claim> result = jwtUtils.extractAllClaims(decodedJWT);

        assertEquals(claims, result);
    }

}
