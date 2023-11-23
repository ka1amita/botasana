package com.resumechatbot.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

@Validated
class ClientPromptValidator implements ConstraintValidator<ClientPrompt, String> {

// TODO figure how to better load external configuratuion and ve able to output expanded Validation error messeages

  @Value(value = "${prompt.validation.length.min}")
  private int min;
  @Value(value = "${prompt.validation.length.max}")
  private int max;

  @Override
  public boolean isValid(String prompt, ConstraintValidatorContext context) {
    return prompt.length() < max && prompt.length() > min;
  }
}