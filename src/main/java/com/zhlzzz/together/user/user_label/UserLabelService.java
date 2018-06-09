package com.zhlzzz.together.user.user_label;

import com.zhlzzz.together.user.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserLabelService {
    UserLabelEntity addUserLabel(Long userId, String label) throws UserLabelUsedException;
    void showUserLabels(Long userId, Set<Long> ids);
    List<UserLabelEntity> getUserLabelsByUserId(Long userId);
    List<UserLabelEntity> getAllByUserId(Long userId);
    void delete(Set<Long> ids);
}
