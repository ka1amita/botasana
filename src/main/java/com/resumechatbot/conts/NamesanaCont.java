package com.resumechatbot.conts;

import com.resumechatbot.services.ChatGptService;
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
  private final ChatGptService chatGptService;

  @Autowired
  public NamesanaCont(ChatGptService chatGptService) {
    this.chatGptService = chatGptService;
  }

  //  ResponseEntity<?> complete(@RequestBody Map<String, String> inputDto) {
//    String prompt = inputDto.get("input");
  @GetMapping
  ResponseEntity<?> complete(@RequestParam String prompt) {
    logger.info("{} has accepted an prompt: \"{}\"", this.getClass().getSimpleName(), prompt);
    String completion = ChatGptService.complete(prompt);
    Map<String, String> response = new HashMap<>();
    response.put("output", completion);
    return ResponseEntity.ok(response);
  }
}
