package com.zhlzzz.together.article.discuss;

import com.google.common.base.Strings;
import com.zhlzzz.together.article.Article;
import com.zhlzzz.together.article.ArticleNotFoundException;
import com.zhlzzz.together.article.ArticleRepository;
import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.data.Slices;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserNotFoundException;
import com.zhlzzz.together.user.UserRepository;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DiscussServiceImpl implements DiscussService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final JdbcTemplate jdbc;
    private final DiscussRepository discussRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;


    @Override
    public Discuss addDiscuss(Long articleId, Long userId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));
        DiscussEntity discussEntity = new DiscussEntity();
        discussEntity.setArticleId(article.getId());
        discussEntity.setUserId(user.getId());
        discussEntity.setContent(content);
        discussEntity.setCreateTime(LocalDateTime.now());
        return discussRepository.save(discussEntity);
    }

    @Override
    public Discuss updateDiscuss(Long id, DiscussParam param) {
        DiscussEntity discussEntity = discussRepository.findById(id).orElseThrow(() -> new DiscussNotFoundException(id));
        if (!Strings.isNullOrEmpty(param.getReplyContent())) {
            discussEntity.setReplyContent(param.getReplyContent());
            discussEntity.setReplyTime(LocalDateTime.now());
        }
        if (param.getAudit() != null) {
            discussEntity.setAudit(param.getAudit());
        }
        if (param.getToTop() != null) {
            discussEntity.setToTop(param.getToTop());
        }
        return discussRepository.save(discussEntity);
    }

    @Override
    public Optional<? extends Discuss> getDiscussById(Long id) {
        return discussRepository.findById(id);
    }

    @Override
    public void deleteDiscuss(Long id) {
        DiscussEntity discussEntity = discussRepository.findById(id).orElseThrow(() -> new DiscussNotFoundException(id));
        tt.execute((s)-> {
            em.createQuery("DELETE FROM DiscussEntity u WHERE u.id = :id")
                    .setParameter("id", discussEntity.getId())
                    .executeUpdate();
            return true;
        });
    }

    @Override
    public Slice<? extends Discuss, Integer> getDiscussesByCriteria(DiscussCriteria criteria, SliceIndicator<Integer> indicator) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DiscussEntity> q = cb.createQuery(DiscussEntity.class);
        Root<DiscussEntity> m = q.from(DiscussEntity.class);

        Predicate where = buildPredicate(cb, m, criteria);
        q.select(m).where(where).orderBy(cb.desc(m.get("toTop")), cb.desc(m.get("createTime")));

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DiscussEntity> countM = countQuery.from(DiscussEntity.class);
        countQuery.select(cb.count(countM)).where(buildPredicate(cb, countM, criteria));
        Slice<DiscussEntity, Integer> slice = Slices.of(em, q, indicator, countQuery);

        return slice.map(DiscussEntity::toDto);
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<DiscussEntity> m, DiscussCriteria criteria) {
        List<Predicate> predicates = new ArrayList<>(3);

        if (criteria.getUserId() != null) {
            predicates.add(cb.equal(m.get("userId"), criteria.getUserId()));
        }
        if (criteria.getArticleId() != null) {
            predicates.add(cb.equal(m.get("articleId"), criteria.getArticleId()));
        }
        if (criteria.getAudit() != null) {
            predicates.add(cb.equal(m.get("audit"), criteria.getAudit()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, DiscussEntity.class)) {
            return;
        }

        try {
            Resource resource = new ClassPathResource("discuss.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, Charset.forName("UTF-8"));

            jdbc.execute(sql);
        } catch (Throwable e) {
            log.error("can not load discuss sql.", e);
        }

    }
}
