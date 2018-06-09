package com.zhlzzz.together.game.game_config;

import com.google.common.base.Strings;
import com.zhlzzz.together.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.Charset;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GameConfigServiceImpl implements GameConfigService {

    @PersistenceContext
    private EntityManager em;
    private final JdbcTemplate jdbc;
    private final TransactionTemplate tt;

    private final GameConfigRepository gameConfigRepository;
    private final GameConfigOptionRepository gameConfigOptionRepository;

    @Override
    public GameConfigEntity addGameConfig(Integer gameTypeId, GameConfig.InputType inputType, String label, Boolean required) {
        GameConfigEntity gameConfigEntity = new GameConfigEntity();
        gameConfigEntity.setGameTypeId(gameTypeId);
        if (inputType != null) {
            gameConfigEntity.setInputType(inputType);
        }
        if (!Strings.isNullOrEmpty(label)) {
            gameConfigEntity.setLabel(label);
        }
        if (required != null) {
            gameConfigEntity.setRequired(required);
        }
        return gameConfigRepository.save(gameConfigEntity);
    }

    @Override
    public GameConfigEntity updateGameConfig(Long id, GameConfig.InputType inputType, String label, Boolean required) {
        GameConfigEntity gameConfigEntity = gameConfigRepository.findById(id).orElseThrow(() -> new GameConfigNotFoundException());
        if (inputType != null) {
            gameConfigEntity.setInputType(inputType);
        }
        if (!Strings.isNullOrEmpty(label)) {
            gameConfigEntity.setLabel(label);
        }
        if (required != null) {
            gameConfigEntity.setRequired(required);
        }
        return gameConfigRepository.save(gameConfigEntity);
    }

    @Override
    public Optional<GameConfigEntity> getGameConfigById(Long id) {
        return gameConfigRepository.findById(id);
    }

    @Override
    public GameConfigOptionEntity addOption(Long configId, String value) {
        GameConfigOptionEntity option = new GameConfigOptionEntity();
        option.setConfigId(configId);
        option.setValue(value);
        return gameConfigOptionRepository.save(option);
    }

    @Override
    public GameConfigOptionEntity updateOption(Long id, String value) {
        GameConfigOptionEntity option = gameConfigOptionRepository.findById(id).orElseThrow(() -> new GameConfigOptionNotFoundException());

        if (!Strings.isNullOrEmpty(value)) {
            option.setValue(value);
        }
        return gameConfigOptionRepository.save(option);
    }

    @Override
    public Optional<GameConfigOptionEntity> getGameConfigOptionById(Long id) {
        return gameConfigOptionRepository.findById(id);
    }

    @Override
    public void deleteGameConfig(Long id) {
        gameConfigRepository.deleteById(id);
    }

    @Override
    public void deleteOption(Long id) {
        gameConfigOptionRepository.deleteById(id);
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, GameConfigEntity.class)) {
            return;
        }

        try {
            Resource resource = new ClassPathResource("game_config.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, Charset.forName("UTF-8"));

            jdbc.execute(sql);

            Resource resource1 = new ClassPathResource("game_config_options.sql");
            byte[] bytes1 = FileCopyUtils.copyToByteArray(resource1.getInputStream());
            String sql1 = new String(bytes1, Charset.forName("UTF-8"));

            jdbc.execute(sql1);
        } catch (Throwable e) {
            log.error("can not load game_config sql.", e);
        }

    }
}
