package com.zhlzzz.together.controllers.user;

import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserService;
import com.zhlzzz.together.user.user_label.UserLabelEntity;
import com.zhlzzz.together.user.user_label.UserLabelService;
import com.zhlzzz.together.user.user_relation.UserRelation;
import com.zhlzzz.together.user.user_relation.UserRelationEntity;
import com.zhlzzz.together.user.user_relation.UserRelationService;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "用户", tags = {"User"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final UserLabelService userLabelService;
    private final UserRelationService userRelationService;

    @GetMapping
    @ApiOperation(value = "用户列表")
    @ResponseBody
    public Slice<? extends UserView, Integer> getArticleList(SliceIndicator<Integer> indicator, ApiAuthentication auth) {
        if (auth.requireUserId() != 1) {
            throw ApiExceptions.badRequest("无权限访问");
        }
        val users = userService.getUsers(indicator);
        return users.mapAll(items -> buildArticlesView(items));
    }

    private List<UserView> buildArticlesView(List<? extends User> users) {
        return CollectionUtils.map(users, (r) ->  new UserView(r,null) );
    }

    @GetMapping(path = "/{userId:\\d+}")
    @ApiOperation(value = "获取指定用户的信息")
    @ResponseBody
    public UserView getUserById(@PathVariable Long userId, ApiAuthentication auth) {
        if (auth.requireUserId() == null) {
            throw ApiExceptions.badRequest("无权限访问");
        }
        User user = userService.getUserById(userId).orElseThrow(() -> ApiExceptions.notFound("不存在此人信息。"));
        List<UserLabelEntity> userLabelEntity = userLabelService.getUserLabelsByUserId(user.getId());
        return new UserView(user, userLabelEntity);
    }

    @GetMapping(path = "/{userId:\\d+}/relations/{relation}")
    @ApiOperation(value = "获取用户关系列表（好友或黑名单）")
    @ResponseBody
    public Slice<? extends UserView, Integer> getUserRelationById(@PathVariable Long userId, @PathVariable UserRelation.Relation relation, SliceIndicator<Integer> indicator) {
        User user = userService.getUserById(userId).orElseThrow(() -> ApiExceptions.notFound("不存在此人信息。"));
        Slice<? extends UserRelation, Integer> userRelations = userRelationService.getUserRelationsByRelation(indicator, user.getId(), relation);

        return userRelations.mapAll(items -> buildUserViews(items));
    }

    private List<UserView> buildUserViews(List<? extends UserRelation> userRelations) {
        return CollectionUtils.map(userRelations, (r) -> buildUserView(r));
    }

    private UserView buildUserView(UserRelation userRelation) {
        User user = userService.getUserById(userRelation.getToUserId()).orElse(null);
        List<UserLabelEntity> userLabels = userLabelService.getUserLabelsByUserId(userRelation.getToUserId());
        return new UserView(user, userLabels);
    }

    @PostMapping(path = "/{userId:\\d+}/relations")
    @ApiOperation(value = "添加好友关系（加好友）")
    @ResponseBody
    public ResponseEntity<String> addRelation(@PathVariable Long userId, @RequestParam Long toUserId, ApiAuthentication auth) {
        if (!auth.requireUserId().equals(userId)) {
            throw ApiExceptions.noPrivilege();
        }
        User user = userService.getUserById(toUserId).orElseThrow(() -> ApiExceptions.notFound("没有相关用户"));
        userRelationService.addUserRelation(userId, user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{userId:\\d+}/relations")
    @ApiOperation(value = "更新好友关系（拉黑或取消拉黑或修改备注名）")
    @ResponseBody
    public ResponseEntity<String> updateRelation(@PathVariable Long userId, @RequestParam Long toUserId, @RequestParam(required = false) String remark, @RequestParam UserRelation.Relation relation) {

        userRelationService.updateUserRelation(userId, toUserId, remark ,relation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
