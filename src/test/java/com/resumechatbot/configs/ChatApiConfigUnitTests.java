package com.resumechatbot.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "openai.api.chat.messages[0].role=\"quoted\"",
    "openai.api.chat.messages[0].content=\"quoted\"",
    "openai.api.chat.model=\"quoted\"",
    "openai.api.chat.temperature=1.5"
})
class ChatApiConfigUnitTests {

  @Autowired
  ChatApiConfig chatApiConfig;

  @Test
  void double_quoted_configuration_values_are_escaped_to_single_quoted_values() {
    assertEquals("'quoted'", chatApiConfig.getMessages().get(0).get("role"));
    assertEquals("'quoted'", chatApiConfig.getMessages().get(0).get("content"));
    assertEquals("'quoted'", chatApiConfig.getModel());
    assertEquals(1.5, chatApiConfig.getTemperature());
  }
}
