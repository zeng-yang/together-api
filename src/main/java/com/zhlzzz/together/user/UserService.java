package com.zhlzzz.together.user;

import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User addUser(UserParam parameters) throws OpenIdUsedException;
    User updateUser(Long id, UserParam parameters);
    Optional<? extends User> getUserById(Long id);
    Optional<? extends User> getUserByOpenId(String openId);
    Set<? extends User> getUsersByIds(Set<Long> ids);
    Set<? extends User> getUsersByOpenIds(Set<String> openIds);
    Slice<? extends User, Integer> getUsers(SliceIndicator<Integer> indicator);
}
