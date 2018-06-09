package com.zhlzzz.together.controllers.rank;

import com.zhlzzz.together.rank.player.PlayerEntity;
import com.zhlzzz.together.rank.player.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/player/{userId:\\d+}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "用户游戏内信息", tags = {"Rank"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerController {

    @Autowired
    private PlayerService palyerService;
    @Autowired
    private Environment env;

    @GetMapping
    @ApiOperation(value = "保存用户游戏内信息")
    @ResponseBody
    public PlayerView addPlayer(@PathVariable Long userId, @RequestParam String nickName, @RequestParam String shardId){
        PlayerEntity playerEntity = palyerService.savePlayer(userId,nickName,shardId,env);
        return new PlayerView(playerEntity);
    }

}
