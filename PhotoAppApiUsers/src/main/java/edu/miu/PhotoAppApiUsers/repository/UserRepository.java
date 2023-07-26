package edu.miu.PhotoAppApiUsers.repository;

import edu.miu.PhotoAppApiUsers.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
