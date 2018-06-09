package com.zhlzzz.together.game;

import com.google.common.base.Strings;
import com.zhlzzz.together.game.game_config.GameConfig;
import com.zhlzzz.together.game.game_config.GameConfigEntity;
import com.zhlzzz.together.game.game_config.GameConfigOptionEntity;
import com.zhlzzz.together.utils.CollectionUtils;
import com.zhlzzz.together.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameTypeServiceImpl implements GameTypeService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final JdbcTemplate jdbc;
    private final GameTypeRepository gameTypeRepository;

    @Override
    public GameType addGameType(String name, String imgUrl, Boolean hot) throws GameTypeNameUsedException {
        GameTypeEntity gameTypeEntity = new GameTypeEntity();
        gameTypeEntity.setName(name);
        gameTypeEntity.setImgUrl(imgUrl);
        if (hot != null) {
            gameTypeEntity.setHot(hot);
        }
        gameTypeEntity.setCreateTime(LocalDateTime.now());
        try {
            return gameTypeRepository.save(gameTypeEntity);
        } catch (DataIntegrityViolationException e) {
            throw new GameTypeNameUsedException(name, e);
        }
    }

    @Override
    public GameType updateGameType(Integer id, String name, String imgUrl, Boolean hot, Boolean deleted) throws GameTypeNotFoundException, GameTypeNameUsedException {
        GameTypeEntity gameTypeEntity = gameTypeRepository.getById(id).orElseThrow(() -> new GameTypeNotFoundException(id));

        if (!Strings.isNullOrEmpty(name)) {
            gameTypeEntity.setName(name);
        }
        if (!Strings.isNullOrEmpty(imgUrl)) {
            gameTypeEntity.setImgUrl(imgUrl);
        }
        if (hot != null) {
            gameTypeEntity.setHot(hot);
        }
        if (deleted != null) {
            gameTypeEntity.setDeleted(deleted);
        }
        try {
            return gameTypeRepository.save(gameTypeEntity);
        } catch (DataIntegrityViolationException e) {
            throw new GameTypeNameUsedException(name, e);
        }
    }

    @Override
    public Optional<? extends GameType> getGameTypeById(Integer id) {
        return gameTypeRepository.getById(id);
    }

    @Override
    public List<? extends GameType> getAllGameTypes() {
        return gameTypeRepository.findAll();
    }

    @Override
    public List<? extends GameConfig> getGameTypeConfigs(Integer gameTypeId) {
        List<GameConfigEntity> gameConfigEntities = em.createQuery("SELECT f FROM GameConfigEntity f WHERE f.gameTypeId = :gameTypeId ORDER BY f.id ASC", GameConfigEntity.class)
                .setParameter("gameTypeId", gameTypeId)
                .getResultList();
        val configMap = new HashMap<Long, GameConfigImpl>();
        val configIds = new ArrayList<Long>(gameConfigEntities.size());
        val topConfigs = new ArrayList<GameConfigImpl>();

        for (GameConfigEntity gameConfigEntity : gameConfigEntities) {
            GameConfigImpl gameConfig = new GameConfigImpl(gameConfigEntity, new ArrayList<>());
            configMap.put(gameConfig.getId(), gameConfig);
            configIds.add(gameConfig.getId());
            topConfigs.add(gameConfig);
        }
        if (!configIds.isEmpty()) {
            loadOptions(configMap, configIds);
        }
        return CollectionUtils.map(topConfigs, GameConfigImpl::toDto);
    }

    private void loadOptions(Map<Long, GameConfigImpl> configMap, List<Long> configIds) {
        val options = em.createQuery("SELECT o from GameConfigOptionEntity o WHERE o.configId IN (:configIds) ORDER BY o.configId ASC, o.id ASC", GameConfigOptionEntity.class)
                .setParameter("configIds", configIds)
                .getResultList();
        GameConfigImpl config = null;
        for (GameConfigOptionEntity option : options) {
            config = configMap.get(option.getConfigId());
            if (config == null) {
                throw new RuntimeException("option entity's field should in the map.");
            }
            CollectionUtils.add(config.getOptions(), option);
        }
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, GameTypeEntity.class)) {
            return;
        }

        try {
            Resource resource = new ClassPathResource("game_type.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, Charset.forName("UTF-8"));

            jdbc.execute(sql);
        } catch (Throwable e) {
            log.error("can not load game_type sql.", e);
        }

    }
}
