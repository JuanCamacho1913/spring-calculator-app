package com.calculator.persistence.repository;

import com.calculator.persistence.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends ListCrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findUserEntityByUsername(String username);
}
