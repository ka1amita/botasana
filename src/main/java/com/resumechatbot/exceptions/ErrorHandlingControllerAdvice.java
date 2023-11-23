package com.resumechatbot.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ErrorHandlingControllerAdvice {

  private final MessageSource messageSource;

  @Autowired
  public ErrorHandlingControllerAdvice(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> handleMethodArgumentNotValidException(
      BindException e) {
    String messages = e.getBindingResult()
        .getAllErrors().stream()
        .map(ObjectError::getDefaultMessage)
        .collect(Collectors.joining(", "));

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", messages);
    errorBody.put("completion",
                  "Oupsasana, " + messageSource.getMessage("error.client_prompt.completion", null,
                                                           LocaleContextHolder.getLocale()));
    return errorBody;
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", e.getMessage());
    return errorBody;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> handleException(
      Exception e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", "Oupsasana");
    return errorBody;
  }
}
