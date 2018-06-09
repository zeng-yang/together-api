package com.zhlzzz.together.validator.constraintvalidators;

import com.zhlzzz.together.validator.constraints.MobilePhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    private static Pattern pattern = Pattern.compile("^(?:134|135|136|137|138|139|147|148|150|151|152|157|158|159|178|182|183|184|187|188|198|130|131|132|145|146|155|156|166|175|176|185|186|133|149|153|173|174|177|180|181|189|199|170|171)[0-9]{8}$");

    @Override
    public void initialize(MobilePhone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
