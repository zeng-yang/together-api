package com.zhlzzz.together.rank.player.player_season;

import com.zhlzzz.together.rank.HttpUtils;
import com.zhlzzz.together.rank.player.PlayerEntity;
import com.zhlzzz.together.rank.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PlayerSeasonServiceImpl {

    private final PlayerSeasonRepository playerSeasonRepository;
    private final PlayerRepository playerRepository;
    private final Environment evn;
    private final EntityManager entityManager;
    private final PlayerBatchAbstractDaoImp playerBatchAbstractDaoImp;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void savePlayerSeason() {
        System.out.println("start save:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //获取所有用户游戏内信息
        Set<PlayerEntity> playerEntities = playerRepository.findAll();
        List<PlayerSeasonEntity> addEntities = new ArrayList<>();
        List<PlayerSeasonEntity> updateEntities = new ArrayList<>();
        playerEntities.forEach(item->{
            PlayerSeasonEntity playerSeasonEntity = playerSeasonRepository.findByUserIdAndSeasonId(item.getUserId(),item.getCurrentSeason()).orElse(new PlayerSeasonEntity());
            playerSeasonEntity.setUserId(item.getUserId());
            playerSeasonEntity.setSeasonId(item.getCurrentSeason());
            Map<String,String> urlVariables = new HashMap<>();
            urlVariables.put("shardId",item.getShardId());
            urlVariables.put("playerId",item.getPlayerId());
            urlVariables.put("seasonId",item.getCurrentSeason());
            String gameModeUrl = evn.getProperty("pubg.baseUrl")+"/shards/{shardId}/players/{playerId}/seasons/{seasonId}";
            ResponseEntity<Object> responseGameModeStats = HttpUtils.getPubgResponse(gameModeUrl,evn.getProperty("pubg.apiKey"),urlVariables);
            Map<String,Object> objectStats = (Map<String, Object>)responseGameModeStats.getBody();
            Map<String, Object> objectMap = (Map<String, Object>) ((Map<String, Object>)objectStats.get("data")).get("attributes");
            Map<String,Object> modeStats = (Map<String,Object>) objectMap.get("gameModeStats");
            modeStats.forEach((k, v)->{
                if ("squad-fpp".equals(k)){
                    playerSeasonEntity.setType(k);
                    Map<String,Object> map = (Map<String, Object>) v;
                    playerSeasonEntity.setAssists((Integer) map.get("assists"));
                    playerSeasonEntity.setKills((Integer)map.get("kills"));
                    playerSeasonEntity.setLosses((Integer)map.get("losses"));
                    playerSeasonEntity.setWinPoints((Double)map.get("winPoints"));
                    playerSeasonEntity.setWins((Integer) map.get("wins"));
                    playerSeasonEntity.setKd(div(playerSeasonEntity.getKills(),playerSeasonEntity.getLosses(),1));
                    if (playerSeasonEntity.getId()!=null) {
                        updateEntities.add(playerSeasonEntity);
                    }else {
                        addEntities.add(playerSeasonEntity);
                    }
                }
            });
        });
        playerBatchAbstractDaoImp.batchUpdate(updateEntities);
        playerBatchAbstractDaoImp.batchInsert(addEntities);
        System.out.println("end save:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    public  double div(double d1,double d2,int len) {// 进行除法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2,len,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
