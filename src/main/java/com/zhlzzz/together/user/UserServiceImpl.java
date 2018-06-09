package com.zhlzzz.together.user;

import com.google.common.base.Strings;
import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.data.Slices;
import com.zhlzzz.together.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserServiceImpl implements UserService {

    @PersistenceContext
    private EntityManager em;
    private final JdbcTemplate jdbc;
    private final UserRepository userRepository;

    private void setParameter(UserEntity userEntity, UserParam parameters) {
        if (!Strings.isNullOrEmpty(parameters.getNickName())) {
            userEntity.setNickName(parameters.getNickName());
        }
        if (!Strings.isNullOrEmpty(parameters.getAvatarUrl())) {
            userEntity.setAvatarUrl(parameters.getAvatarUrl());
        }
        if (parameters.getGender() != null) {
            userEntity.setGender(parameters.getGender());
        }
        if (!Strings.isNullOrEmpty(parameters.getOpenId())) {
            userEntity.setOpenId(parameters.getOpenId());
        }
        if (!Strings.isNullOrEmpty(parameters.getUnionId())) {
            userEntity.setUnionId(parameters.getUnionId());
        }
        if (!Strings.isNullOrEmpty(parameters.getPhone())) {
            userEntity.setPhone(parameters.getPhone());
        }
        if (!Strings.isNullOrEmpty(parameters.getQRCode())) {
            userEntity.setQRCode(parameters.getQRCode());
        }
    }

    @Override
    public User addUser(UserParam parameters) throws OpenIdUsedException {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(parameters.getRole());
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setLastLoginTime(LocalDateTime.now());
        setParameter(userEntity, parameters);

        try {
            return userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new OpenIdUsedException(parameters.getPhone(), e);
        }

    }

    @Override
    public User updateUser(Long id, UserParam parameters) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        setParameter(userEntity, parameters);
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<? extends User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<? extends User> getUserByOpenId(String openId) {
        return userRepository.findByOpenId(openId);
    }

    @Override
    public Set<? extends User> getUsersByIds(Set<Long> ids) {
        return userRepository.findByIdIn(ids);
    }

    @Override
    public Set<? extends User> getUsersByOpenIds(Set<String> openIds) {
        return userRepository.findByOpenIdIn(openIds);
    }

    @Override
    public Slice<? extends User, Integer> getUsers(SliceIndicator<Integer> indicator) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> q = cb.createQuery(UserEntity.class);
        Root<UserEntity> m = q.from(UserEntity.class);

        Predicate where = buildPredicate(cb, m);
        q.select(m).where(where).orderBy(cb.desc(m.get("createTime")), cb.desc(m.get("id")));

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<UserEntity> countM = countQuery.from(UserEntity.class);

        countQuery.select(cb.count(countM)).where(cb.and(buildPredicate(cb, countM)));

        Slice<UserEntity, Integer> slice = Slices.of(em, q, indicator, countQuery);
        return slice.map(UserEntity::toDto);
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<UserEntity> m) {
        List<Predicate> predicates = new ArrayList<>(1);


        predicates.add(cb.equal(m.get("role"), User.Role.user));

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, UserEntity.class)) {
            return;
        }

        try {
            Resource resource = new ClassPathResource("users.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, Charset.forName("UTF-8"));

            jdbc.execute(sql);
        } catch (Throwable e) {
            log.error("can not load user sql.", e);
        }

    }
}
