package com.resumechatbot.conts;

import com.resumechatbot.services.ChatCompletionService;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated // for the validation of request parameters
@RestController
@RequestMapping("/namesana")
public class NamesanaCont {

  static final Logger logger = LoggerFactory.getLogger(NamesanaCont.class);
  private final ChatCompletionService chatCompletionService;
  private final int promptMaxLength = 20;

  @Autowired
  public NamesanaCont(ChatCompletionService chatCompletionService) {
    this.chatCompletionService = chatCompletionService;
  }

  @GetMapping
  ResponseEntity<Map<String, String>> complete(@RequestParam @Size(
      min = 1, max = promptMaxLength, // TODO move the validation to Service layer?
      message = "'prompt' parameter size must be between 1 and 20") String prompt) {

    String chatCompletion = chatCompletionService.complete(prompt);
    Map<String, String> response = new HashMap<>();
    response.put("completion", chatCompletion);
    return ResponseEntity.ok(response);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(
      Exception e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", e.getMessage());
    return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ValidationException.class)
    // doesn't catch with ConstraintViolationException.classs!
  ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ValidationException e) {

    Map<String, String> errorBody = new HashMap<>();
    errorBody.put("error", e.getMessage().replaceAll("^complete.prompt: ", ""));
    return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
  }
}
