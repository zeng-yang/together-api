package com.zhlzzz.together.controllers.rank;

import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.rank.RankEntity;
import com.zhlzzz.together.rank.RankService;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserNotFoundException;
import com.zhlzzz.together.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/rank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "排行榜", tags = {"Rank"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankController {

    @Autowired
    private RankService rankService;
    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "获取排行榜列表")
    @ResponseBody
    public List<RankEntity> getPlayer(ApiAuthentication auth){
        User user = userService.getUserById(auth.requireUserId()).orElseThrow(() -> ApiExceptions.notFound("没有相关用户信息"));
        return rankService.findRankList(user.getId());
    }



}
