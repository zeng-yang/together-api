package com.zhlzzz.together.user;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends Repository<UserEntity, Long> {

    UserEntity save(UserEntity userEntity);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByOpenId(String openId);
    Set<UserEntity> findByIdIn(Set<Long> ids);
    Set<UserEntity> findByOpenIdIn(Set<String> openIds);
}
