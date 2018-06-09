package com.zhlzzz.together.auth.password;

import com.zhlzzz.together.utils.EntityUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserPasswordServiceImpl implements UserPasswordService {

    @PersistenceContext
    private EntityManager em;
    @NonNull
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate tt;

    @Override
    public void updateUserPassword(Long userId, String password) {
        tt.execute((s)->{
            Query query = em.createNamedQuery(PasswordEntity.UPDATE_QUERY_NAME);
            query.setParameter("userId", userId);
            query.setParameter("hashedPassword", passwordEncoder.encode(password));
            query.setParameter("updateTime", LocalDateTime.now());
            query.executeUpdate();
            return null;
        });
    }

    @Override
    public boolean verifyUserPassword(Long userId, String password) {
        PasswordEntity entity = em.find(PasswordEntity.class, userId);
        return entity != null && passwordEncoder.matches(password, entity.getHashedPassword());
    }

    @Override
    public String getUserHashedPassword(Long userId) {
        PasswordEntity entity = em.find(PasswordEntity.class, userId);
        if (entity == null) {
            return null;
        }
        return entity.getHashedPassword();
    }

    @Override
    public LocalDateTime getUserUpdateTime(Long userId) {
        PasswordEntity entity = em.find(PasswordEntity.class, userId);
        if (entity == null) {
            return null;
        }
        return entity.getUpdateTime();
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, PasswordEntity.class)) {
            return;
        }

        tt.execute((s) -> {
            updateUserPassword(1L, "Abc123");
            updateUserPassword(2L, "user");
            return null;
        });
    }
}
