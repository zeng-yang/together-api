package com.zhlzzz.together.article.advert;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AdvertServiceImpl implements AdvertService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final JdbcTemplate jdbc;
    private final AdvertRepository advertRepository;

    private void setParamter(AdvertEntity advertEntity,AdvertParam advertParam){
        if (!Strings.isNullOrEmpty(advertParam.getTitle())) {
            advertEntity.setTitle(advertParam.getTitle());
        }
        if (!Strings.isNullOrEmpty(advertParam.getAdvertUrl())) {
            advertEntity.setAdvertUrl(advertParam.getAdvertUrl());
        }
        if (advertParam.getArticleId() != null) {
            advertEntity.setArticleId(advertParam.getArticleId());
        }

    }

    @Override
    public Advert addAdvert(AdvertParam advertParam) {
        AdvertEntity advertEntity = new AdvertEntity();
        setParamter(advertEntity, advertParam);
        advertEntity.setCreateTime(LocalDateTime.now());
        return advertRepository.save(advertEntity);
    }

    @Override
    public Advert updateAdvert(Long id, AdvertParam advertParam) {
        AdvertEntity advert = advertRepository.findById(id).orElseThrow(() -> new AdvertNotFoundException(id));
        setParamter(advert,advertParam);
        return advertRepository.save(advert);
    }

    @Override
    public Optional<? extends Advert> getAdvertById(Long id) {
        return advertRepository.findById(id);
    }

    @Override
    public void deleteAdvert(Long id) {
        AdvertEntity replyEntity = advertRepository.findById(id).orElseThrow(() -> new AdvertNotFoundException(id));
        tt.execute((s)-> {
            em.createQuery("DELETE FROM AdvertEntity u WHERE u.id = :id")
                    .setParameter("id", replyEntity.getId())
                    .executeUpdate();
            return true;
        });
    }

    @Override
    public List<AdvertEntity> findAll(){
        return advertRepository.findAll();
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, AdvertEntity.class)) {
            return;
        }

        try {
            Resource resource = new ClassPathResource("advert.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, Charset.forName("UTF-8"));

            jdbc.execute(sql);
        } catch (Throwable e) {
            log.error("can not load advert sql.", e);
        }

    }
}
