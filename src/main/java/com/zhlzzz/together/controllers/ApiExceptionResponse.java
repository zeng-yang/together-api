package com.zhlzzz.together.controllers;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionResponse {
    private String error;
    private String message;
}
