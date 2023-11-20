package com.resumechatbot.conts;

import com.resumechatbot.services.ChatGptServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/namesana")
public class NamesanaCont {

  static final Logger logger = LoggerFactory.getLogger(NamesanaCont.class);
  private final ChatGptServiceImpl chatGptService;

  @Autowired
  public NamesanaCont(ChatGptServiceImpl chatGptService) {
    this.chatGptService = chatGptService;
  }

  @GetMapping
  ResponseEntity<?> complete(@RequestParam String prompt) {
    String completion = chatGptService.complete(prompt);
    Map<String, String> response = new HashMap<>();
    response.put("output", completion);
    return ResponseEntity.ok(response);
  }
}
