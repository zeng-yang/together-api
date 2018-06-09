package com.zhlzzz.together.article;

import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;

import java.util.Optional;

public interface ArticleService {

    Article addArticle(ArticleParam article);
    Article updateArticle(Long id, ArticleParam article);
    Optional<? extends Article> getArticleById(Long id);
    Slice<? extends Article, Integer> getArticles(SliceIndicator<Integer> indicator);
    void deleteArticle(Long id);
}
