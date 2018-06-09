package com.zhlzzz.together.controllers.game;

import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.game.GameType;
import com.zhlzzz.together.game.GameTypeService;
import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserService;
import com.zhlzzz.together.user.user_game_config.UserGameConfigEntity;
import com.zhlzzz.together.user.user_game_config.UserGameConfigService;
import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/games/game-types", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "游戏类别", tags = {"Game"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameTypeController {

    private final GameTypeService gameTypeService;
    private final UserService userService;
    private final UserGameConfigService userGameConfigService;

    @PostMapping
    @ApiOperation(value = "新增游戏类别")
    @ResponseBody
    public GameTypeBaseView create(@RequestParam String name, @RequestParam String imgUrl, @RequestParam(required = false) boolean hot, ApiAuthentication auth) {
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        requireNonNull(name, "name");
        requireNonNull(imgUrl, "imgUrl");
        return new GameTypeBaseView(gameTypeService.addGameType(name,imgUrl,hot), true);
    }

    @PutMapping(path = "{id:\\d+}")
    @ApiOperation(value = "更新游戏类别")
    @ResponseBody
    public GameTypeBaseView update(@PathVariable Integer id, @RequestParam(required = false) String name, @RequestParam(required = false) String imgUrl, @RequestParam(required = false) boolean hot, @RequestParam(required = false) boolean delete, ApiAuthentication auth) {
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        GameType gameType = gameTypeService.getGameTypeById(id).orElseThrow(()-> ApiExceptions.notFound("不存在此游戏类型"));

        return new GameTypeBaseView(gameTypeService.updateGameType(gameType.getId(),name,imgUrl,hot,delete), true);
    }

    @GetMapping
    @ApiOperation(value = "获取游戏类别列表")
    @ResponseBody
    public List<GameTypeBaseView> getGameTypes(ApiAuthentication auth) {
        List<? extends GameType> gameTypes = gameTypeService.getAllGameTypes();
        Boolean isShow = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).isPresent();

        return CollectionUtils.map(gameTypes, (r) -> new GameTypeBaseView(r, isShow));

    }

    @GetMapping(path = "/{id:\\d+}}")
    @ApiOperation(value = "获取游戏配置项列表")
    @ResponseBody
    public List<GameConfigView> getGameConfig(@PathVariable Integer id, ApiAuthentication auth) {
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        List<? extends GameConfig> gameConfigs = gameTypeService.getGameTypeConfigs(id);

        return CollectionUtils.map(gameConfigs,(r) -> new GameConfigView(r));
    }

    @GetMapping(path = "/{id:\\d+}/config")
    @ApiOperation(value = "获取对应游戏类别的配置")
    @ResponseBody
    public GameTypeView getGameTypeConfigs(@PathVariable Integer id, ApiAuthentication auth) {
        GameType gameType = gameTypeService.getGameTypeById(id).orElseThrow(()-> ApiExceptions.notFound("不存在此游戏类型"));
        List<? extends GameConfig> gameConfigs = gameTypeService.getGameTypeConfigs(gameType.getId());
        UserGameConfigEntity userGameConfigEntity = userGameConfigService.getUserGameConfigByUserAndGameType(auth.requireUserId(), gameType.getId()).orElse(null);
        List<? extends UserMatchConfig> userMatchConfigs = null;
        if (userGameConfigEntity != null) {
            userMatchConfigs = userGameConfigService.getUserMatchConfigByUserGameConfigId(userGameConfigEntity.getId());
        }
        return new GameTypeView(gameType, true,gameConfigs,userGameConfigEntity,userMatchConfigs);
    }

    private void requireNonNull(Object value, String name) {
        if (value == null) {
            throw ApiExceptions.missingParameter(name);
        }
    }

}
