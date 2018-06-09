package com.zhlzzz.together.controllers;

import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ApiExceptions {

    static ApiException unknownException() {
        return new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "系统错误，请稍后重试。", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static ApiException notFound(@Nonnull String message) {
        return new ApiException(HttpStatus.NOT_FOUND.getReasonPhrase(), message, HttpStatus.NOT_FOUND);
    }

    static ApiException badRequest(@Nonnull String message) {
        return new ApiException(HttpStatus.BAD_REQUEST.getReasonPhrase(), message, HttpStatus.BAD_REQUEST);
    }

    static ApiException invalidParameter(@Nonnull String name) {
        return invalidParameter(name, null);
    }

    static ApiException invalidParameter(@Nonnull String name, @Nullable String value) {
        String message = value == null ?
                String.format("无效参数: %s", name) :
                String.format("无效参数: %s, 值: %s", name, value);
        return new ApiException("invalid parameter", message, HttpStatus.BAD_REQUEST);
    }

    static ApiException missingParameter(@Nonnull String name) {
        String message = String.format("缺少参数: %s", name);
        return new ApiException("missing parameter", message, HttpStatus.BAD_REQUEST);
    }

    static ApiException unauthorized() {
        return new ApiException("unauthorized", "还没授权。", HttpStatus.UNAUTHORIZED);
    }

    static ApiException noPrivilege() {
        return new ApiException("no privilege", "权限不够。", HttpStatus.BAD_REQUEST);
    }

    static ApiException needLogin() {
        return new ApiException("unauthorized", "请先登录。", HttpStatus.UNAUTHORIZED);
    }

    static ApiException violatePolicy(@Nonnull String message) {
        return new ApiException(HttpStatus.BAD_REQUEST.getReasonPhrase(), message, HttpStatus.BAD_REQUEST);
    }

}
