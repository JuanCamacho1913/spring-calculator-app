package com.calculator.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "create_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_enable")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credencial_no_expired")
    private boolean credentialNoExpired;

    @PrePersist
    public void prePersist(){
        if (id == null){
            this.id = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
    }
}

