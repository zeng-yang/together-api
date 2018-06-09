package com.zhlzzz.together.user.user_label;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserLabelRepository extends Repository<UserLabelEntity, Long> {
    UserLabelEntity save(UserLabelEntity userLabelEntity);
    Optional<UserLabelEntity> findById(Long id);
    List<UserLabelEntity> findAllByUserId(Long userId);
    List<UserLabelEntity> findByUserIdAndShowedOrderByIdAsc(Long userId, boolean showed);
}
