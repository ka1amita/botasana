package com.resumechatbot.exceptions;

import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Map<String, String> onConstraintValidationException(
      ValidationException e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", e.getMessage().replaceAll("^complete.prompt: ", ""));
    return errorBody;
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(
      Exception e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", e.getMessage());
    return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
  }
}
