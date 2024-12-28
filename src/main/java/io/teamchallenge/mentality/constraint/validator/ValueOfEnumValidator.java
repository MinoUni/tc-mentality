package io.teamchallenge.mentality.constraint.validator;

import io.teamchallenge.mentality.constraint.ValueOfEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

  private Set<String> enumValues;

  @Override
  public void initialize(ValueOfEnum constraintAnnotation) {
    enumValues =
        Stream.of(constraintAnnotation.enumClass().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value != null && enumValues.contains(value.toString().toUpperCase())) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate("must be any of %s".formatted(enumValues))
        .addConstraintViolation();
    return false;
  }
}
