package com.resumechatbot.conts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.resumechatbot.services.ChatCompletionService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class NamesanaContUnitTests {

  @Mock
  ChatCompletionService chatCompletionService;
  @InjectMocks
  NamesanaCont namesanaCont;

  @Test
  public void returns_response_with_status_200_and_body_with_completion() {
    // arrange
    String completion = "completion";
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    // act
    ResponseEntity<Map<String, String>> response = namesanaCont.complete("any");
    // assert
    assertEquals(completion, response.getBody().get("completion"));
    assertEquals(200, response.getStatusCode().value());
  }
}
