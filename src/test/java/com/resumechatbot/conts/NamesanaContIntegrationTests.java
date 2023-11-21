package com.resumechatbot.conts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class NamesanaContIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void returns_() throws Exception {
    this.mockMvc.perform(get("/namesana").param("prompt", "Test"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.completion").value("Test"));
  }
}
