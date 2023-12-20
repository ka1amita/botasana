package com.botasana.utils;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ClientPromptValidator.class)
@Documented
public @interface ClientPrompt {

  String message() default "invalid 'prompt' parameter";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
