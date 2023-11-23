package com.resumechatbot.conts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumechatbot.dtos.PromptDto;
import com.resumechatbot.services.ChatCompletionService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BotasanaCont.class)
class BotasanaContWebLayerTests {

  @Value(value = "${prompt.validation.length.max}")
  private int promptMaxLength;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ChatCompletionService chatCompletionService;

  @Test
  void endpoint_returns_response_with_status_200_and_json_completion() throws Exception {
    String completion = "completion";
    PromptDto prompt = new PromptDto();
    prompt.setPrompt("Test");
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(post("/botasana")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(new ObjectMapper().writeValueAsBytes(prompt)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.completion").value(completion));
  }

  @Test
  void endpoint_fails_with_missing_prompt_request_key() throws Exception {
    String completion = "completion";
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(post("/botasana"))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void endpoint_fails_with_different_request_key() throws Exception {
    String completion = "completion";
    Map<String, String> prompt = new HashMap<>();
    prompt.put("different", "Test");
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(post("/botasana")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(new ObjectMapper().writeValueAsBytes(prompt)))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void endpoint_fails_with_empty_prompt_request_key() throws Exception {
    String completion = "completion";
    Map<String, String> prompt = new HashMap<>();
    prompt.put("prompt", "");
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(post("/botasana")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(new ObjectMapper().writeValueAsBytes(prompt)))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("invalid 'prompt' parameter provided"));
  }

  @Test
  void app_returns_error_json_to_a_too_long_prompt() throws Exception {

    StringBuilder builder = new StringBuilder();
    builder.append('l');
    for (int i = 0; i < promptMaxLength - 2; i++) {
      builder.append('o');
    }
    builder.append("ng");
    String oneChartooLongString = builder.toString();
    assertEquals(promptMaxLength + 1, oneChartooLongString.length());

    Map<String, String> prompt = new HashMap<>();
    prompt.put("prompt", oneChartooLongString);
    this.mockMvc.perform(post("/botasana")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(new ObjectMapper().writeValueAsBytes(prompt)))
        .andDo(print())
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.error").value("invalid 'prompt' parameter provided"));
  }
}
