package com.zhlzzz.together.controllers.wx;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.google.common.base.Strings;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.zhlzzz.together.auth.password.UserPasswordService;
import com.zhlzzz.together.controllers.ApiExceptions;
import com.zhlzzz.together.qiniu.QiNiuService;
import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserParam;
import com.zhlzzz.together.user.UserService;
import com.zhlzzz.together.user.user_label.UserLabelEntity;
import com.zhlzzz.together.user.user_label.UserLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/wx", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(description = "微信登录", tags = {"Wx"})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WxAuthController {


    private final WxMaService wxMaService;
    private final WxMaQrcodeService wxMaQrcodeService;
    private final UserService userService;
    private final UserLabelService userLabelService;
    private final UserPasswordService userPasswordService;
    private final QiNiuService qiNiuService;

    @GetMapping(path = "/login")
    @ApiOperation(value = "小程序登录前注册并获取用户信息")
    @ResponseBody
    public UserselfView login(String code, String rawData, String signature, String encryptedData, String iv) {
        try {
            WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(code);
            User user = userService.getUserByOpenId(result.getOpenid()).orElse(null);
            if (user != null) {
                List<UserLabelEntity> userLabelEntitys = userLabelService.getUserLabelsByUserId(user.getId());
                return new UserselfView(user, userLabelEntitys);
            } else {
                if (!wxMaService.getUserService().checkUserInfo(result.getSessionKey(), rawData, signature)) {
                    throw ApiExceptions.invalidParameter("rawData");
                }

                WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(result.getSessionKey(), encryptedData, iv);
                user = addUser(userInfo);
                userPasswordService.updateUserPassword(user.getId(),user.getOpenId());
                return new UserselfView(user, null);
            }
        } catch (WxErrorException e) {
            throw ApiExceptions.invalidParameter("code");
        }

    }

    private User addUser(WxMaUserInfo userInfo) {
        UserParam userParam = new UserParam();
        if (!Strings.isNullOrEmpty(userInfo.getNickName())) {
            userParam.setNickName(userInfo.getNickName());
        }
        if (!Strings.isNullOrEmpty(userInfo.getAvatarUrl())) {
            userParam.setAvatarUrl(userInfo.getAvatarUrl());
        }
        if (!Strings.isNullOrEmpty(userInfo.getGender())) {
            userParam.setGender(Integer.parseInt(userInfo.getGender()));
        }
        if (!Strings.isNullOrEmpty(userInfo.getOpenId())) {
            userParam.setOpenId(userInfo.getOpenId());
//            userParam.setQRCode(getUserQRCode(userInfo.getOpenId()));
        }
        if (!Strings.isNullOrEmpty(userInfo.getUnionId())) {
            userParam.setUnionId(userInfo.getUnionId());
        }
        userParam.setRole(User.Role.user);
        userParam.setQRCode("http://p9mhxf2ng.bkt.clouddn.com/qrcode.png");
        return userService.addUser(userParam);
    }

    private String getUserQRCode(String openId) {
        try {
            File file = wxMaQrcodeService.createWxCodeLimit(openId, "pages/kankan/kankan");
            return file.getPath();
        } catch (WxErrorException e) {
            throw ApiExceptions.invalidParameter("openId");
        }
    }

//    private String getPath(File file) {
//        try {
//            Response response = qiNiuService.uploadFile(file);
//        } catch (QiniuException e) {
//            throw ApiExceptions.invalidParameter("file");
//        }
//    }

//    @GetMapping(path = "/qrcode")
//    @ApiOperation(value = "获取用户微信二维码")
//    @ResponseBody
//    public String getCode(String code) {
//        try {
//            File file = wxMaQrcodeService.createWxCodeLimit(code, "pages/index/index");
//            return file.getPath();
//        } catch (WxErrorException e) {
//            throw ApiExceptions.invalidParameter("openId");
//        }
//    }
}
