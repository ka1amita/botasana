package com.resumechatbot.exceptions;

import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> handleValidationException(
      ValidationException e) {

    Map<String, String> errorBody = new HashMap<>();
    // TODO figute out how to catch the exception without the additional string
    errorBody.put("error", e.getMessage().replaceAll("^complete.prompt: ", ""));
    return errorBody;
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> handleMissingServletRequestParameterException(
      Exception e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", e.getMessage());
    return errorBody;
  }
}
