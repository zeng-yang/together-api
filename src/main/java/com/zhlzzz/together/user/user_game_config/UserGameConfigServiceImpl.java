package com.zhlzzz.together.user.user_game_config;

import com.google.common.base.Strings;
import com.zhlzzz.together.game.GameTypeEntity;
import com.zhlzzz.together.game.GameTypeNotFoundException;
import com.zhlzzz.together.game.GameTypeRepository;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserNotFoundException;
import com.zhlzzz.together.user.UserRepository;
import com.zhlzzz.together.user.user_match_config.UserMatchConfig;
import com.zhlzzz.together.user.user_match_config.UserMatchConfigEntity;
import com.zhlzzz.together.user.user_match_config.UserMatchConfigImpl;
import com.zhlzzz.together.user.user_match_config.UserMatchConfigParam;
import com.zhlzzz.together.utils.CollectionUtils;
import com.zhlzzz.together.utils.EntityUtils;
import com.zhlzzz.together.utils.Functional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserGameConfigServiceImpl implements UserGameConfigService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final UserGameConfigRepository userGameConfigRepository;
    private final UserRepository userRepository;
    private final GameTypeRepository gameTypeRepository;


    @Override
    public UserGameConfigEntity updateUserGameConfig(Long userId, Integer gameTypeId, UserGameConfigParam userGameConfigParam) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        GameTypeEntity gameType = gameTypeRepository.getById(gameTypeId).orElseThrow(() -> new GameTypeNotFoundException(gameTypeId));
        return tt.execute((s)-> {

            UserGameConfigEntity userGameConfigEntity = userGameConfigRepository.getByUserIdAndGameTypeId(userId, gameTypeId).orElse(null);
            if (userGameConfigEntity == null) {
                userGameConfigEntity = new UserGameConfigEntity();
                userGameConfigEntity.setUserId(user.getId());
                userGameConfigEntity.setGameTypeId(gameType.getId());
                if (!Strings.isNullOrEmpty(userGameConfigParam.getNickname())) {
                    userGameConfigEntity.setNickname(userGameConfigParam.getNickname());
                }
                if (!Strings.isNullOrEmpty(userGameConfigParam.getContact())) {
                    userGameConfigEntity.setContact(userGameConfigParam.getContact());
                }
                if (!Strings.isNullOrEmpty(userGameConfigParam.getArea())) {
                    userGameConfigEntity.setArea(userGameConfigParam.getArea());
                }
                em.persist(userGameConfigEntity);
            } else {
                userGameConfigEntity.setUserId(user.getId());
                userGameConfigEntity.setGameTypeId(gameType.getId());
                if (!Strings.isNullOrEmpty(userGameConfigParam.getNickname())) {
                    userGameConfigEntity.setNickname(userGameConfigParam.getNickname());
                }
                if (!Strings.isNullOrEmpty(userGameConfigParam.getContact())) {
                    userGameConfigEntity.setContact(userGameConfigParam.getContact());
                }
                if (!Strings.isNullOrEmpty(userGameConfigParam.getArea())) {
                    userGameConfigEntity.setArea(userGameConfigParam.getArea());
                }
            }
            em.flush();

            return userGameConfigEntity;
        });
    }

    @Override
    public Optional<UserGameConfigEntity> getUserGameConfigByUserAndGameType(Long userId, Integer gameTypeId) {
        return userGameConfigRepository.getByUserIdAndGameTypeId(userId, gameTypeId);
    }

    @Override
    public Set<UserGameConfigEntity> getUserGameConfigsByGameType(Integer gameTypeId) {
        return userGameConfigRepository.getAllByGameTypeId(gameTypeId);
    }

    @Override
    public List<? extends UserMatchConfig> updateUserMatchConfig(Long userGameConfigId, List<UserMatchConfigParam> params) {
        return tt.execute((s)-> {
            val userGameConfig = em.find(UserGameConfigEntity.class, userGameConfigId, LockModeType.PESSIMISTIC_WRITE);
            if (userGameConfig == null) {
                return Collections.emptyList();
            }
            val userMatchConfigs = createUserMatchConfigs(userGameConfigId,params);
            clearUserMatchConfig(userGameConfigId);
            val values = saveUserMatchConfigs(userMatchConfigs);
            em.flush();
            return CollectionUtils.map(values, UserMatchConfigImpl::toDto);
        });
    }

    @Override
    public List<? extends UserMatchConfig> getUserMatchConfigByUserGameConfigId(Long userGameConfigId) {
        val valueEntities = em.createQuery("SELECT v from UserMatchConfigEntity v WHERE v.userGameConfigId = :userGameConfigId ORDER BY v.gameConfigId ASC, v.id ASC", UserMatchConfigEntity.class)
                .setParameter("userGameConfigId", userGameConfigId)
                .getResultList();
        val values = new ArrayList<UserMatchConfigImpl>();
        for (UserMatchConfigEntity userMatchConfigEntity : valueEntities) {
            UserMatchConfigImpl value = new UserMatchConfigImpl(userMatchConfigEntity);
            values.add(value);
        }


        return CollectionUtils.map(values, UserMatchConfigImpl::toDto);
    }

    private List<UserMatchConfigImpl> createUserMatchConfigs(Long userGameConfigId, List<UserMatchConfigParam> params) {
        if (params == null) {
            return null;
        }
        return params.stream().map((p)->
                createUserMatchConfig(userGameConfigId, p)
        ).filter((p)->
                p != null &&
                p.getOptionId() != null
        ).collect(Collectors.toList());

    }

    private UserMatchConfigImpl createUserMatchConfig(Long userGameConfigId, UserMatchConfigParam param) {
        if (param.getGameConfigId() == null) {
            throw new IllegalArgumentException("UserMatchConfigParam field id should not be null.");
        }
        if (param.getOptionId() == null) {
            return null;
        }

        val userMatchConfigEntity = new UserMatchConfigEntity();
        userMatchConfigEntity.setUserGameConfigId(userGameConfigId);
        userMatchConfigEntity.setGameConfigId(param.getGameConfigId());
        Functional.setIfNotNull(param::getOptionId, userMatchConfigEntity::setOptionId);
        return new UserMatchConfigImpl(userMatchConfigEntity);
    }

    private void clearUserMatchConfig(Long userGameConfigId) {
        em.createQuery("DELETE FROM UserMatchConfigEntity v WHERE v.userGameConfigId = :userGameConfigId")
                .setParameter("userGameConfigId", userGameConfigId)
                .executeUpdate();
    }

    private List<UserMatchConfigImpl> saveUserMatchConfigs(List<UserMatchConfigImpl> userMatchConfigs) {
        val valueEntities = new ArrayList<UserMatchConfigEntity>();

        for (UserMatchConfigImpl userMatchConfig : userMatchConfigs) {
            valueEntities.add(userMatchConfig.getUserMatchConfigEntity());
        }
        EntityUtils.batchPersist(em, valueEntities);
        em.flush();
        return userMatchConfigs;
    }
}
