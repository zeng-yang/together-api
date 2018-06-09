package com.zhlzzz.together.match;

import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.data.Slices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MatchServiceImpl implements MatchService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final MatchRepository matchRepository;

    @Override
    public Match addMatch(Long userId, Integer gameTypeId, Long minute, String formId, Boolean onlyFriend) {
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setUserId(userId);
        matchEntity.setGameTypeId(gameTypeId);
        matchEntity.setFormId(formId);
        if (onlyFriend != null) {
            matchEntity.setOnlyFriend(onlyFriend);
        } else {
            matchEntity.setOnlyFriend(false);
        }
        matchEntity.setCreateTime(LocalDateTime.now());
        matchEntity.setExpiration(LocalDateTime.now().plusMinutes(minute));
        return matchRepository.save(matchEntity);
    }

    @Override
    public Optional<? extends Match> getCurrentMatchByUser(Long userId) {
//        MatchEntity matchEntity = em.createQuery("SELECT m FROM MatchEntity m WHERE m.userId = :userId AND " +
//                "m.finished = false AND m.deleted = false AND m.expiration > :currentTime ORDER BY m.createTime desc", MatchEntity.class)
//                .setParameter("userId", userId)
//                .setParameter("currentTime", LocalDateTime.now())
//                .getSingleResult();
        return matchRepository.getFirstByUserIdOrderByCreateTimeDesc(userId);
    }

    @Override
    public Slice<? extends Match, Integer> getMatchs(SliceIndicator<Integer> indicator) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MatchEntity> q = cb.createQuery(MatchEntity.class);
        Root<MatchEntity> m = q.from(MatchEntity.class);

        q.select(m).orderBy(cb.desc(m.get("createTime")), cb.desc(m.get("id")));

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<MatchEntity> countM = countQuery.from(MatchEntity.class);

        List<Predicate> predicates = new ArrayList<>(5);
        countQuery.select(cb.count(countM)).where(cb.and(predicates.toArray(new Predicate[0])));

        Slice<MatchEntity, Integer> slice = Slices.of(em, q, indicator, countQuery);
        return slice.map(MatchEntity::toDto);
    }

    @Override
    public Boolean finish(Long id) {
        MatchEntity matchEntity = matchRepository.getById(id).orElseThrow(() -> new MatchNotFoundException(id));
        matchEntity.setFinished(true);
        matchRepository.save(matchEntity);
        return true;
    }

    @Override
    public Boolean delete(Long id) {
        MatchEntity matchEntity = matchRepository.getById(id).orElseThrow(() -> new MatchNotFoundException(id));
        matchEntity.setDeleted(true);
        matchRepository.save(matchEntity);
        return true;
    }

    @Override
    public List<? extends Match> getMatchsInUserIds(Set<Long> userIds) {
        return matchRepository.getByUserIdInAndDeleted(userIds, false);
    }

    @Override
    public List<? extends Match> getMatchsByUserIdsInAndEffective(Set<Long> userIds, Integer gameTypeId) {
        List<MatchEntity> matchEntitys = em.createQuery("SELECT m FROM MatchEntity m WHERE m.userId in :userIds AND " +
                "m.finished = false AND m.gameTypeId = :gameTypeId AND m.deleted = false AND m.expiration > :currentTime ORDER BY m.createTime desc", MatchEntity.class)
                .setParameter("userIds", userIds)
                .setParameter("gameTypeId", gameTypeId)
                .setParameter("currentTime", LocalDateTime.now())
                .getResultList();
        return matchEntitys;
    }
}
