package com.zhlzzz.together.controllers.system;

import com.zhlzzz.together.controllers.ApiAuthentication;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.system.feedback.FeedbackEntity;
import com.zhlzzz.together.system.feedback.FeedbackParam;
import com.zhlzzz.together.system.feedback.FeedbackService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "意见反馈", tags = {"Feedback"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @PostMapping(path = "/feedbacks")
    @ApiOperation(value = "发送意见反馈")
    @ResponseBody
    public ResponseEntity<String> saveFeedback(@Valid @RequestBody FeedbackParam feedbackParam, BindingResult result, ApiAuthentication auth) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream().map((e)->e.toString()).collect(Collectors.joining(";\n"));
            throw ApiExceptions.badRequest(errors);
        }

        feedbackService.addFeedback(auth.requireUserId(), feedbackParam);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping(path = "/feedbacks/{id:\\d+}")
    @ApiOperation(value = "已处理意见反馈")
    @ResponseBody
    public void finishedFeedback(@PathVariable Long id, ApiAuthentication auth) {
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        feedbackService.finishedFeedback(id);
    }

    @GetMapping(path = "/feedbacks")
    @ApiOperation(value = "获取反馈意见列表")
    @ResponseBody
    public List<FeedbackView> getFeedbackList(ApiAuthentication auth) {
        User admin = userService.getUserById(auth.requireUserId()).filter(u -> u.isAdmin()).orElse(null);
        if (admin == null) {
            throw ApiExceptions.noPrivilege();
        }
        List<FeedbackEntity> feedbackEntities = feedbackService.findAll();
        return CollectionUtils.map(feedbackEntities,(r) -> new FeedbackView(r));
    }

}
