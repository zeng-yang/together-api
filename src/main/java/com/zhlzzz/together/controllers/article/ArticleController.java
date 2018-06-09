package com.zhlzzz.together.controllers.article;

import com.google.common.base.Strings;
import com.zhlzzz.together.article.Article;
import com.zhlzzz.together.article.ArticleParam;
import com.zhlzzz.together.article.ArticleService;
import com.zhlzzz.together.article.discuss.Discuss;
import com.zhlzzz.together.article.discuss.DiscussCriteria;
import com.zhlzzz.together.article.discuss.DiscussService;
import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserService;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/articles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "资讯", tags = {"Article"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController {

    private final ArticleService articleService;
    private final DiscussService discussService;
    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "文章列表")
    @ResponseBody
    public Slice<? extends ArticleListView, Integer> getArticleList(SliceIndicator<Integer> indicator) {
        val articles = articleService.getArticles(indicator);
        return articles.mapAll(items -> buildArticlesView(items));
    }

    @GetMapping(path = "/{id:\\d+}")
    @ApiOperation(value = "文章详情")
    @ResponseBody
    public ArticleView getArticle(@PathVariable Long id) {
        Article article = articleService.getArticleById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此文章。"));
        return new ArticleView(article);
    }

    @PostMapping
    @ApiOperation(value = "新建文章")
    @ResponseBody
    public ArticleView addArticle( @Valid @RequestBody ArticleParam parameters, ApiAuthentication auth, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream().map((e)->e.toString()).collect(Collectors.joining(";\n"));
            throw ApiExceptions.badRequest(errors);
        }
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        requireNonNull(parameters.getTitle(),"title");
        Article article = articleService.addArticle(parameters);
        return new ArticleView(article);
    }

    @PutMapping(path = "/{id:\\d+}")
    @ApiOperation(value = "更新文章")
    @ResponseBody
    public ArticleView updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleParam parameters, ApiAuthentication auth, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream().map((e)->e.toString()).collect(Collectors.joining(";\n"));
            throw ApiExceptions.badRequest(errors);
        }
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        Article article = articleService.getArticleById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此文章"));

        return new ArticleView(articleService.updateArticle(article.getId(), parameters));
    }

    @DeleteMapping(path = "/{id:\\d+}")
    @ApiOperation(value = "删除文章")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Long id, ApiAuthentication auth) {
        Article article = articleService.getArticleById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此文章"));

        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        articleService.deleteArticle(article.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void requireNonNull(Object value, String name) {
        if (value == null) {
            throw ApiExceptions.missingParameter(name);
        }
    }

    private List<ArticleListView> buildArticlesView(List<? extends Article> articles) {
        return CollectionUtils.map(articles, (r) ->  new ArticleListView(r) );
    }

    @PostMapping(path = "/{id:\\d+}/discusses")
    @ApiOperation(value = "新增评论")
    public DiscussView addDiscuss(@PathVariable Long id, @RequestParam String content, ApiAuthentication apiAuth) {
        Article article = articleService.getArticleById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此文章"));
        if (Strings.isNullOrEmpty(content)) {
            throw ApiExceptions.invalidParameter("content");
        }
        Long userId = apiAuth.requireUserId();
        User user = userService.getUserById(userId).orElse(null);
        Discuss discuss = discussService.addDiscuss(article.getId(), userId, content);
        return new DiscussView(discuss, user);
    }

    @GetMapping(path = "/{id:\\d+}/discusses")
    @ApiOperation(value = "获取文章评论")
    public Slice<? extends DiscussView, Integer> getArticlesDiscusses(@PathVariable Long id, SliceIndicator<Integer> indicator, ApiAuthentication auth) {
        Article article = articleService.getArticleById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此文章"));
        DiscussCriteria criteria = new DiscussCriteria();
        criteria.setArticleId(article.getId());
        criteria.setAudit(true);
        val discusses = discussService.getDiscussesByCriteria(criteria, indicator);
        return discusses.mapAll(items -> buildDiscussViews(items));
    }

    protected List<DiscussView> buildDiscussViews(List<? extends Discuss> discusses) {
        Set<Long> userIds = new HashSet<>();
        for (Discuss discuss : discusses) {
            userIds.add(discuss.getUserId());
        }
        Set<? extends User> users = userService.getUsersByIds(userIds);
        Map<Long, ? extends User> userMap = CollectionUtils.toMap(users, User::getId);

        return CollectionUtils.map(discusses, (d) -> {
            User user = userMap.get(d.getUserId());
            return new DiscussView(d, user);});
    }
}
