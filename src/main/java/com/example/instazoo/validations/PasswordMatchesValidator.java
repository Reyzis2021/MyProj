package com.example.instazoo.validations;

import com.example.instazoo.annotations.PasswordMatches;
import com.example.instazoo.payload.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignUpRequest signUpRequest = (SignUpRequest) obj;
        return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
    }
}
