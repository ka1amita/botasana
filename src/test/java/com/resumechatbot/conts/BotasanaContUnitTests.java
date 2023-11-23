package com.resumechatbot.conts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.resumechatbot.dtos.PromptDto;
import com.resumechatbot.services.ChatCompletionService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BotasanaContUnitTests {

  @Mock
  ChatCompletionService chatCompletionService;
  @InjectMocks
  BotasanaCont botasanaCont;

  @Test
  public void returns_response_with_status_200_and_body_with_completion() {
    // arrange
    String completion = "completion";
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    PromptDto prompt = new PromptDto();
    prompt.setPrompt("any");
    // act
    Map<String, String> response = botasanaCont.completePost(prompt);
    // assert
    assertEquals(completion, response.get("completion"));
  }
}
