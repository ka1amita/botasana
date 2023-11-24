package com.resumechatbot.conts;

import com.resumechatbot.dtos.PromptDto;
import com.resumechatbot.services.ChatCompletionService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class BotasanaCont {

  static final Logger logger = LoggerFactory.getLogger(BotasanaCont.class);
  private final ChatCompletionService chatCompletionService;
  @Value(value = "${prompt.validation.length.max}")
  private int maxPromptLength;

  @Autowired
  public BotasanaCont(ChatCompletionService chatCompletionService) {
    this.chatCompletionService = chatCompletionService;
  }

  @GetMapping("/")
  public String getIndex(Model model) {
    model.addAttribute("maxPromptLength", maxPromptLength);
    return "index";
  }

  @PostMapping("/botasana")
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  Map<String, String> completePost(@Valid @RequestBody PromptDto prompt) {
    // TODO validate prompt
    String chatCompletion = chatCompletionService.complete(prompt);
    Map<String, String> response = new HashMap<>();
    response.put("completion", chatCompletion);
    return response;
  }
}
