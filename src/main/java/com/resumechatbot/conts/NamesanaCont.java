package com.resumechatbot.conts;

import com.resumechatbot.dtos.PromptDto;
import com.resumechatbot.services.ChatCompletionService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class NamesanaCont {

  static final Logger logger = LoggerFactory.getLogger(NamesanaCont.class);
  private final ChatCompletionService chatCompletionService;

  @Autowired
  public NamesanaCont(ChatCompletionService chatCompletionService) {
    this.chatCompletionService = chatCompletionService;
  }

  @GetMapping("/")
  public String getIndex() {
    return "index";
  }

  @PostMapping("/namesana")
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  Map<String, String> completePost(@Valid @RequestBody PromptDto prompt) {
    // TODO validate prompt
    String chatCompletion = chatCompletionService.complete(prompt.getPrompt());
    Map<String, String> response = new HashMap<>();
    response.put("completion", chatCompletion);
    return response;
  }
}
