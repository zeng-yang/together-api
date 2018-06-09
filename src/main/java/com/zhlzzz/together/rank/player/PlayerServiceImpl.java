package com.zhlzzz.together.rank.player;

import com.zhlzzz.together.rank.HttpUtils;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserNotFoundException;
import com.zhlzzz.together.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository palyerRepository;
    private final UserRepository userRepository;

    @Override
    public PlayerEntity savePlayer(Long userId, String playerName, String shardId, Environment evn) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
        PlayerEntity playerEntity = palyerRepository.findByUserId(user.getId()).orElse(new PlayerEntity());
        playerEntity.setUserId(user.getId());
        playerEntity.setPlayerName(playerName);
        playerEntity.setShardId(shardId);
        Map<String,String> urlVariables = new HashMap<String,String>();
        urlVariables.put("shardId",shardId);
        urlVariables.put("playerName",playerName);

        //获取用户游戏内ID
        String playerUrl =evn.getProperty("pubg.baseUrl")+"/shards/{shardId}/players?filter[playerNames]={playerName}";
        ResponseEntity<Object> responsePlayer = HttpUtils.getPubgResponse(playerUrl,evn.getProperty("pubg.apiKey"),urlVariables);
        Map<String,Object> objectPlayer = (Map<String, Object>) responsePlayer.getBody();
        List<Map<String, Object>> playerInfo = (List<Map<String, Object>>) objectPlayer.get("data");
        playerEntity.setPlayerId((String) playerInfo.get(0).get("id"));

        //获取当前赛季ID
        String seasonUrl = evn.getProperty("pubg.baseUrl")+"/shards/{shardId}/seasons";
        ResponseEntity<Object>  responseSeason = HttpUtils.getPubgResponse(seasonUrl,evn.getProperty("pubg.apiKey"),urlVariables);
        Map<String,Object> objectSeason = (Map<String, Object>) responseSeason.getBody();
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) objectSeason.get("data");
        mapList.forEach(item->{
            Map<String,Boolean> attributes= (Map<String,Boolean>) item.get("attributes");
            if (attributes.get("isCurrentSeason") && !attributes.get("isOffseason"))
                playerEntity.setCurrentSeason((String) item.get("id"));
        });

        return palyerRepository.save(playerEntity);
    }


}
