package com.zhlzzz.together.auth.password;

import java.time.LocalDateTime;

public interface UserPasswordService {
    void updateUserPassword(Long userId, String password);
    boolean verifyUserPassword(Long userId, String password);
    String getUserHashedPassword(Long userId);
    LocalDateTime getUserUpdateTime(Long userId);
}
