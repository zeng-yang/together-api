package com.zhlzzz.together.user.user_game_config;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserGameConfigParam implements Serializable {


    private String nickname;

    private String area;

    private String contact;
}
