package com.gophagi.nanugi.groupbuying.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidValidator implements ConstraintValidator<EnumValid, Enum<?>> {
	@Override
	public void initialize(EnumValid constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
		return value != null;
	}
}
