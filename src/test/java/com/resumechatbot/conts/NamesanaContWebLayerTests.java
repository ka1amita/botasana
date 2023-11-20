package com.resumechatbot.conts;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.resumechatbot.services.ChatCompletionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NamesanaCont.class)
class NamesanaContWebLayerTests {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ChatCompletionService chatCompletionService;

  @Test
  void endpoint_returns_response_with_status_200_and_json_completion() throws Exception {
    String completion = "completion";
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(get("/namesana").param("prompt", "any"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.completion").value(completion));
  }

  @Test
  void endpoint_fails_without_any_request_parameter() throws Exception {
    String completion = "completion";
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(get("/namesana"))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }

  @Test
  void endpoint_fails_wit_invalid_request_parameter() throws Exception {
    String completion = "completion";
    when(chatCompletionService.complete(any(String.class))).thenReturn(completion);
    this.mockMvc.perform(get("/namesana").param("invalid", "any"))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }
}
