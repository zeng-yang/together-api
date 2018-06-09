package com.zhlzzz.together.validator.constraintvalidators;

import com.zhlzzz.together.validator.constraints.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, Object> {

    private String passwordReg = "/^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,24}$/";
    private Pattern passwordPattern = Pattern.compile(passwordReg);

    @Override
    public void initialize(Password password) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || "".equals(value)) {
            return false;
        }
        return passwordPattern.matcher(value.toString()).matches();
    }
}
