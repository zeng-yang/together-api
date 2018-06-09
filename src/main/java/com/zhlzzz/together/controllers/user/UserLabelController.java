package com.zhlzzz.together.controllers.user;

import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.user.user_label.UserLabelEntity;
import com.zhlzzz.together.user.user_label.UserLabelService;
import com.zhlzzz.together.utils.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/users/{userId:\\d+}/labels", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "用户标签", tags = {"UserLabel"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserLabelController {

    private final UserLabelService userLabelService;

    @GetMapping
    @ApiOperation(value = "获取用户全部标签")
    @ResponseBody
    public List<UserLabelView> getUserLabels(@PathVariable Long userId) {
        List<UserLabelEntity> userLabels = userLabelService.getAllByUserId(userId);

        return CollectionUtils.map(userLabels, (r) -> new UserLabelView(r) );
    }

    @PostMapping
    @ApiOperation(value = "新建用户标签")
    @ResponseBody
    public UserLabelView addLabel(@RequestParam String label, @PathVariable Long userId, ApiAuthentication auth) {
        if (!auth.requireUserId().equals(userId)) {
            throw ApiExceptions.noPrivilege();
        }
        UserLabelEntity userLabelEntity =  userLabelService.addUserLabel(userId, label);
        return new UserLabelView(userLabelEntity);
    }

    @PutMapping
    @ApiOperation(value = "更新用户标签是否展示")
    @ResponseBody
    public ResponseEntity<String> updateLabel(@PathVariable Long userId, @RequestParam Set<Long> ids, ApiAuthentication auth) {
        if (!auth.requireUserId().equals(userId)) {
            throw ApiExceptions.noPrivilege();
        }
        userLabelService.showUserLabels(userId, ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户标签")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Long userId, @RequestParam Set<Long> ids, ApiAuthentication auth) {
        if (!auth.requireUserId().equals(userId)) {
            throw ApiExceptions.noPrivilege();
        }
        userLabelService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
