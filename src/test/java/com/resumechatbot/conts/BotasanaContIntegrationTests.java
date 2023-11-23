package com.resumechatbot.conts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumechatbot.dtos.PromptDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

//@ActiveProfiles("integration-tests") in favour of @SpringBootTest(properties = ...)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "openai.api.chat.temperature=1.0",
    "openai.api.chat.messages[0].role=system",
    "openai.api.chat.messages[0].content=Repeat"
})
class BotasanaContIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void app_returns_completion_to_a_valid_prompt() throws Exception {
    PromptDto prompt = new PromptDto();
    prompt.setPrompt("Test");
    this.mockMvc.perform(post("/botasana")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(new ObjectMapper().writeValueAsBytes(prompt)))
        // TODO run this integration (and other costly) tests last after oll other cussed!
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.completion").value("Test"));
  }
}