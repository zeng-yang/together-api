package com.zhlzzz.together.article;

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
public class ArticleServiceImpl implements ArticleService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final JdbcTemplate jdbc;
    private final ArticleRepository articleRepository;

    private void setParameter(ArticleEntity article, ArticleParam parameters) {
        if (!Strings.isNullOrEmpty(parameters.getAuthor())) {
            article.setAuthor(parameters.getAuthor());
        }
        if (!Strings.isNullOrEmpty(parameters.getTitle())) {
            article.setTitle(parameters.getTitle());
        }
        if (!Strings.isNullOrEmpty(parameters.getImgUrl())) {
            article.setImgUrl(parameters.getImgUrl());
        }
        if (!Strings.isNullOrEmpty(parameters.getContent())) {
            article.setContent(parameters.getContent());
        }
        if (!Strings.isNullOrEmpty(parameters.getIntroduction())) {
            article.setIntroduction(parameters.getIntroduction());
        }
    }

    @Override
    public Article addArticle(ArticleParam parameters) {
        ArticleEntity article = new ArticleEntity();
        setParameter(article, parameters);
        article.setCreateTime(LocalDateTime.now());
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long id, ArticleParam parameters) {
        ArticleEntity article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        setParameter(article, parameters);
        return articleRepository.save(article);
    }

    @Override
    public Optional<? extends Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Slice<? extends Article, Integer> getArticles(SliceIndicator<Integer> indicator) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ArticleEntity> q = cb.createQuery(ArticleEntity.class);
        Root<ArticleEntity> m = q.from(ArticleEntity.class);

        q.select(m).orderBy(cb.desc(m.get("createTime")), cb.desc(m.get("id")));

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ArticleEntity> countM = countQuery.from(ArticleEntity.class);

        List<Predicate> predicates = new ArrayList<>(5);
        countQuery.select(cb.count(countM)).where(cb.and(predicates.toArray(new Predicate[0])));

        Slice<ArticleEntity, Integer> slice = Slices.of(em, q, indicator, countQuery);
        return slice.map(ArticleEntity::toDto);
    }

    @Override
    public void deleteArticle(Long id) {
        ArticleEntity articleEntity = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        tt.execute((s)-> {
            em.createQuery("DELETE FROM ArticleEntity u WHERE u.id = :id")
                    .setParameter("id", articleEntity.getId())
                    .executeUpdate();
            return true;
        });
    }

    @PostConstruct
    public void onStartUp() {
        if (!EntityUtils.isEntitiesEmpty(em, ArticleEntity.class)) {
            return;
        }

        try {
            Resource resource = new ClassPathResource("article.sql");
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String sql = new String(bytes, Charset.forName("UTF-8"));

            jdbc.execute(sql);
        } catch (Throwable e) {
            log.error("can not load article sql.", e);
        }

    }
}
