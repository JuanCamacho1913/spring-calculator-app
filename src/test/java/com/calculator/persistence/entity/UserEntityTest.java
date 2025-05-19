package com.calculator.persistence.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserEntityTest {

    @Test
    void prePersistTest() {
        UUID id = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(id);

        user.prePersist();

        assertEquals(id, user.getId());
    }

    @Test
    void prePersistTimestamp() {
        UserEntity user = new UserEntity();
        user.getId();
        user.getUsername();
        user.getEmail();
        user.getCreatedAt();

        user.prePersist();

        assertNotNull(user.getId(), "ID should be generated");
        assertNotNull(user.getCreatedAt(), "Timestamp should be set");
    }

    @Test
    @DisplayName("Should set the attributes")
    void allSetters() {
        UUID id = UUID.randomUUID();
        String username = "johndoe";
        String password = "securepass";
        String email = "johndoe@example.com";
        LocalDateTime createdAt = LocalDateTime.now();

        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(createdAt);
        user.setEnabled(true);
        user.setAccountNoExpired(true);
        user.setAccountNoLocked(false);
        user.setCredentialNoExpired(true);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(createdAt, user.getCreatedAt());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNoExpired());
        assertFalse(user.isAccountNoLocked());
        assertTrue(user.isCredentialNoExpired());
    }
}
