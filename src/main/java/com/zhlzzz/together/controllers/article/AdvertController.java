package com.zhlzzz.together.controllers.article;

import com.zhlzzz.together.article.advert.Advert;
import com.zhlzzz.together.article.advert.AdvertEntity;
import com.zhlzzz.together.article.advert.AdvertParam;
import com.zhlzzz.together.article.advert.AdvertService;
import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/advert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "广告", tags = {"Advert"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertController {

    private final AdvertService advertService;
    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "获取广告列表")
    @ResponseBody
    public List<AdvertView> getAdvertList() {
        List<AdvertEntity> advertEntities = advertService.findAll();

        return CollectionUtils.map(advertEntities,(r) -> new AdvertView(r));
    }

    @PostMapping
    @ApiOperation(value = "新增广告")
    @ResponseBody
    public AdvertView addAdvert(@Valid @RequestBody AdvertParam advertParam, ApiAuthentication auth, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream().map((e)->e.toString()).collect(Collectors.joining(";\n"));
            throw ApiExceptions.badRequest(errors);
        }
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        Advert advert = advertService.addAdvert(advertParam);
        return new AdvertView(advert);
    }

    @PutMapping(path = "/{id:\\d+}")
    @ApiOperation(value = "更新广告")
    @ResponseBody
    public AdvertView updateAdvert(@PathVariable Long id, @Valid @RequestBody AdvertParam advertParam, ApiAuthentication auth, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream().map((e)->e.toString()).collect(Collectors.joining(";\n"));
            throw ApiExceptions.badRequest(errors);
        }
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        Advert advert = advertService.getAdvertById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此广告"));

        return new AdvertView(advertService.updateAdvert(advert.getId(), advertParam));
    }

    @DeleteMapping(path = "/{id:\\d+}")
    @ApiOperation(value = "删除广告")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Long id,  ApiAuthentication auth) {
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        Advert advertEntity = advertService.getAdvertById(id).orElseThrow(() -> ApiExceptions.notFound("不存在此广告"));
        advertService.deleteAdvert(advertEntity.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
