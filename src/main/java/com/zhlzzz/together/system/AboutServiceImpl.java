package com.zhlzzz.together.system;

import com.google.common.base.Strings;
import com.zhlzzz.together.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AboutServiceImpl implements AboutService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final AboutRepository aboutRepository;

    @Override
    public AboutEntity updateAbout(String company, String logo, String introduction) {
        return tt.execute((s)-> {
            Integer id = 1;
            AboutEntity aboutEntity = em.find(AboutEntity.class, id, LockModeType.PESSIMISTIC_WRITE);
            if (aboutEntity == null) {
                aboutEntity = new AboutEntity();
                if (!Strings.isNullOrEmpty(introduction)) {
                    aboutEntity.setIntroduction(introduction);
                }
                if (!Strings.isNullOrEmpty(company)) {
                    aboutEntity.setCompany(company);
                }
                if (!Strings.isNullOrEmpty(logo)) {
                    aboutEntity.setLogo(logo);
                }
                em.persist(aboutEntity);
            } else {
                if (!Strings.isNullOrEmpty(introduction)) {
                    aboutEntity.setIntroduction(introduction);
                }
                if (!Strings.isNullOrEmpty(company)) {
                    aboutEntity.setCompany(company);
                }
                if (!Strings.isNullOrEmpty(logo)) {
                    aboutEntity.setLogo(logo);
                }
            }
            em.flush();
            return getAbout();
        });

    }

    @Override
    public AboutEntity getAbout() {
        return aboutRepository.findById(1).orElse(null);
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, AboutEntity.class)) {
            return;
        }

        tt.execute((s) -> {
            updateAbout("组起", "http://p6rwlbhj0.bkt.clouddn.com/image/together/logo.jpg", "介绍介绍");
            return null;
        });

    }
}
