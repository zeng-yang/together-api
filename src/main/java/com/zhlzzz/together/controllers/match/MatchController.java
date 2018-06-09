package com.zhlzzz.together.controllers.match;

import com.zhlzzz.together.chat_room.ChatRoomService;
import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.game.GameType;
import com.zhlzzz.together.game.GameTypeService;
import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.game.game_config.GameConfigOptionEntity;
import com.zhlzzz.together.game.game_config.GameConfigService;
import com.zhlzzz.together.match.Match;
import com.zhlzzz.together.match.MatchService;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.user_game_config.UserGameConfigEntity;
import com.zhlzzz.together.user.user_game_config.UserGameConfigService;
import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import com.zhlzzz.together.user.user_relation.UserRelation;
import com.zhlzzz.together.user.user_relation.UserRelationService;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import java.util.*;

@RestController
@RequestMapping(path = "/match", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "匹配", tags = {"Match"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MatchController {

    private final MatchService matchService;
    private final UserGameConfigService userGameConfigService;
    private final ChatRoomService chatRoomService;
    private final UserRelationService userRelationService;
    private final GameTypeService gameTypeService;
    private final GameConfigService gameConfigService;

    @PostMapping
    @ApiOperation(value = "新建匹配")
    @ResponseBody
    public MatchView addMatch(@RequestParam Integer gameTypeId, @RequestParam Long minute, @RequestParam String formId, @RequestParam Boolean onlyFriend, ApiAuthentication auth) {
        UserGameConfigEntity userGameConfigEntity = userGameConfigService.getUserGameConfigByUserAndGameType(auth.requireUserId(), gameTypeId).orElseThrow(() -> ApiExceptions.missingParameter("config"));
        List<? extends UserMatchConfig> userMatchConfigs = userGameConfigService.getUserMatchConfigByUserGameConfigId(userGameConfigEntity.getId());
        if (userMatchConfigs.size() < 3) {
            throw ApiExceptions.missingParameter("config");
        }
        Match match = matchService.getCurrentMatchByUser(auth.requireUserId()).orElse(null);
        if (match == null) {
            match = matchService.addMatch(auth.requireUserId(), gameTypeId,minute,formId,onlyFriend);
        } else {
            throw ApiExceptions.badRequest("当前正在匹配中，无法新增匹配");
        }
        matchFriends(match);
        return new MatchView(match);
    }

    @DeleteMapping
    @ApiOperation(value = "取消匹配")
    public void deleteMatch(ApiAuthentication auth) {
        Match match = matchService.getCurrentMatchByUser(auth.requireUserId()).orElseThrow(() -> ApiExceptions.notFound("没有相关匹配"));
        if (match.isFinished() == false && match.isDeleted() == false) {
            matchService.delete(match.getId());
        }
    }

    @GetMapping
    @ApiOperation(value = "获取匹配记录")
    public Slice<? extends MatchView, Integer> getMatchList(SliceIndicator<Integer> indicator) {
        val matchs = matchService.getMatchs(indicator);
        return matchs.mapAll(items -> buildMatchViews(items));
    }

    private List<MatchView> buildMatchViews(List<? extends Match> matches) {
        return CollectionUtils.map(matches, (r) -> new MatchView(r));
    }


    private boolean matchFriends(Match match) {
        List<? extends UserRelation> userRelations = userRelationService.getUserRelationsByUserIdAndRelation(match.getUserId(), UserRelation.Relation.friend);
        Set<Long> userIds = new HashSet<>();
        for (UserRelation userRelation : userRelations) {
            userIds.add(userRelation.getToUserId());
        }
        if (userRelations.size() < 3 && match.isOnlyFriend() == true) {
            return false;
        } else if (match.isOnlyFriend() == true){

            if (matchGame(match, userIds)) {
                return true;
            }
        } else {
            return matchFriendsAndOther(match, userIds);
        }
        return false;
    }

    private boolean matchFriendsAndOther(Match match, Set<Long> userIds) {

        Set<? extends UserRelation> userRelations = userRelationService.getUserRelationsByUserIdsInAndRelation(userIds, UserRelation.Relation.friend);
        for (UserRelation userRelation : userRelations) {
            if (!userRelation.getToUserId().equals(match.getUserId())) {
                userIds.add(userRelation.getToUserId());
            }
        }
        matchGame(match, userIds);
        return false;
    }

    private boolean matchGame(Match match, Set<Long> userIds) {
        Integer total = 0;
        Set<Match> matchs = new HashSet<>();

        if (userIds.contains(match.getUserId())) {
            userIds.remove(match.getUserId());
        }

        List<? extends GameConfig> gameConfigs = gameTypeService.getGameTypeConfigs(match.getGameTypeId());

        UserGameConfigEntity userselfGameConfigEntity = userGameConfigService.getUserGameConfigByUserAndGameType(match.getUserId(), match.getGameTypeId()).orElse(null);
        List<? extends UserMatchConfig> userselfMatchConfigs = userGameConfigService.getUserMatchConfigByUserGameConfigId(userselfGameConfigEntity.getId());
        GameConfigOptionEntity gameConfigOptionEntity = gameConfigService.getGameConfigOptionById(userselfMatchConfigs.get(0).getOptionId()).orElse(null);
        Integer totals = Integer.parseInt(gameConfigOptionEntity.getValue());

        List<? extends Match> matches = matchService.getMatchsByUserIdsInAndEffective(userIds, match.getGameTypeId());
        for (Match userMatch : matches) {
            UserGameConfigEntity userGameConfigEntity = userGameConfigService.getUserGameConfigByUserAndGameType(userMatch.getUserId(), userMatch.getGameTypeId()).orElse(null);
            List<? extends UserMatchConfig> userMatchConfigs = userGameConfigService.getUserMatchConfigByUserGameConfigId(userGameConfigEntity.getId());
            if (isMatch(userselfMatchConfigs, userMatchConfigs, gameConfigs)) {
                total++;
                matchs.add(userMatch);
            }
            if (total.equals(totals)) {
                matchs.add(match);
                finished(matchs, match.getGameTypeId());
                return true;
            }
        }

        return false;

    }

    private boolean isMatch(List<? extends UserMatchConfig> userselfMatchConfigs, List<? extends UserMatchConfig> userMatchConfigs, List<? extends GameConfig> gameConfigs) {
        if (userselfMatchConfigs.isEmpty() || userMatchConfigs.isEmpty()) {
            return false;
        }
        if (userselfMatchConfigs.size() != userMatchConfigs.size()) {
            return false;
        }
        Long option1 = 0L;
        Long option2 = 0L;
        HashSet<Long> option3 = new HashSet<>();
        boolean isHas = false;
        for (GameConfig gameConfig : gameConfigs) {
            if (gameConfig.getInputType().equals(GameConfig.InputType.radio)) {
                for (UserMatchConfig userMatchConfig : userselfMatchConfigs) {
                    if (userMatchConfig.getGameConfigId().equals(gameConfig.getId())) {
                        option1 = userMatchConfig.getOptionId();
                        break;
                    }
                }
                for (UserMatchConfig userMatchConfig : userMatchConfigs) {
                    if (userMatchConfig.getGameConfigId().equals(gameConfig.getId())) {
                        option2 = userMatchConfig.getOptionId();
                        break;
                    }
                }
                if (option1 == 0L || option2 == 0L || !option1.equals(option2)) {
                    return false;
                }
            } else if (gameConfig.getInputType().equals(GameConfig.InputType.checkbox)) {
                isHas = false;
                for (UserMatchConfig userMatchConfig : userselfMatchConfigs) {
                    if (userMatchConfig.getGameConfigId().equals(gameConfig.getId())) {
                        option3.add(userMatchConfig.getOptionId());
                    }
                }
                for (UserMatchConfig userMatchConfig : userMatchConfigs) {
                    if (userMatchConfig.getGameConfigId().equals(gameConfig.getId())) {
                        if (option3.contains(userMatchConfig.getOptionId())) {
                            isHas = true;
                        }
                    }
                }
                if (!isHas) {
                    return isHas;
                }
            }
        }
        return true;
    }

    private void finished(Set<Match> matchs, Integer gameTypeId) {
        Set<Long> userIds = new HashSet<>();

        for (Match match : matchs) {
            matchService.finish(match.getId());
            userIds.add(match.getUserId());
        }

        GameType gameType = gameTypeService.getGameTypeById(gameTypeId).orElse(null);

        chatRoomService.addChatRoom(gameType.getName(), userIds);

    }



}
