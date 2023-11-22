package com.resumechatbot.conts;

import com.resumechatbot.services.ChatCompletionService;
import com.resumechatbot.utils.ClientPrompt;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

  @Autowired
  public NamesanaCont(ChatCompletionService chatCompletionService) {
    this.chatCompletionService = chatCompletionService;
  }

  @GetMapping
  ResponseEntity<Map<String, String>> complete(@RequestParam @ClientPrompt String prompt) {
    // TODO move the validation to Service layer?

    String chatCompletion = chatCompletionService.complete(prompt);
    Map<String, String> response = new HashMap<>();
    response.put("completion", chatCompletion);
    return ResponseEntity.ok(response);
  }
}
