package com.zhlzzz.together.rank;

import com.zhlzzz.together.rank.player.PlayerEntity;
import com.zhlzzz.together.rank.player.PlayerNotFoundException;
import com.zhlzzz.together.rank.player.PlayerRepository;
import com.zhlzzz.together.rank.player.player_season.PlayerSeasonEntity;
import com.zhlzzz.together.rank.player.player_season.PlayerSeasonNotFoundException;
import com.zhlzzz.together.rank.player.player_season.PlayerSeasonRepository;
import com.zhlzzz.together.user.user_relation.UserRelation;
import com.zhlzzz.together.user.user_relation.UserRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RankServiceImp implements RankService {

    private final PlayerRepository playerRepository;
    private final PlayerSeasonRepository playerSeasonRepository;
    private final UserRelationRepository userRelationRepository;

    @Override
    public List<RankEntity> findRankList(Long userId) {
        List<? extends UserRelation> listFriend = userRelationRepository.findByUserIdAndRelation(userId, UserRelation.Relation.friend);
        List<RankEntity> rankEntityList = new ArrayList<>();
        listFriend.forEach(item->{
           rankEntityList.add(getRankEntity(item.getUserId()));
        });
        rankEntityList.add(getRankEntity(userId));
        return sort(rankEntityList);
    }

    public RankEntity getRankEntity(Long userId){
        PlayerEntity playerEntity = playerRepository.findByUserId(userId).orElseThrow(()->new PlayerNotFoundException(userId));
        PlayerSeasonEntity playerSeasonEntity = playerSeasonRepository.findByUserIdAndSeasonId(userId,playerEntity.getCurrentSeason()).orElseThrow(()->new PlayerSeasonNotFoundException(userId));
        return  setParam(playerEntity,playerSeasonEntity);
    }
    public RankEntity setParam(PlayerEntity playerEntity,PlayerSeasonEntity playerSeasonEntity){
        RankEntity rankEntity = new RankEntity();
        if (playerSeasonEntity.getKd() !=null)
            rankEntity.setKd(playerSeasonEntity.getKd());
        if (playerSeasonEntity.getWinPoints() != null)
            rankEntity.setRating(playerSeasonEntity.getWinPoints());
        rankEntity.setId(playerSeasonEntity.getId());
        rankEntity.setUserId(playerEntity.getUserId());
        rankEntity.setPlayerName(playerEntity.getPlayerName());
        return rankEntity;
    }

    public List sort(List list){
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1,Object o2){
                RankEntity r1 = (RankEntity) o1;
                RankEntity r2 = (RankEntity) o2;
                if (r1.getKd() > r2.getKd())
                    return 1;
                if (r1.getKd() == r2.getKd())
                    return 0;
                return -1;
            }
        });
        return list;
    }
}
