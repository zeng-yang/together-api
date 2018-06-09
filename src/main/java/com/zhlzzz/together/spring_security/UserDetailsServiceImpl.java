package com.zhlzzz.together.spring_security;

import com.zhlzzz.together.auth.password.UserPasswordService;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserNotFoundException;
import com.zhlzzz.together.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;
    private UserPasswordService passwordService;
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource
            .getAccessor();

    @Autowired
    public UserDetailsServiceImpl(UserService userService, UserPasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByOpenId(username)
                .orElseThrow(()->new UserNotFoundException(username));

        String hashedPassword = passwordService.getUserHashedPassword(user.getId());

        return new UserDetailImpl(user.getId(), hashedPassword);
    }
}
