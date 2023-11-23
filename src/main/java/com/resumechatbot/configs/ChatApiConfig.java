package com.resumechatbot.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "openai.api.chat")
public class ChatApiConfig {

  private final List<Map<String, String>> messages;
  private final String model;
  private final float temperature;

  @ConstructorBinding
  public ChatApiConfig(List<Map<String, String>> messages, String model, float temperature) {
    this.model = escapePrompt(model);
    this.messages = List.of(escapePrompt(messages).toArray(new Map[]{})); // unmodifiable list!
    this.temperature = temperature;
  }

  public List<Map<String, String>> getMessages() {
    return messages;
  }

  public String getModel() {
    return model;
  }

  public float getTemperature() {
    return temperature;
  }

  private List<Map<String, String>> escapePrompt(List<Map<String, String>> messages) {
    List<Map<String, String>> escapedMessages = new ArrayList<>();
    for (Map<String, String> message : messages) {
      Map<String, String> escapedMessage = new HashMap<>();
      for (Entry<String, String> entry : message.entrySet()) {
        escapedMessage.put(entry.getKey(), entry.getValue().replaceAll("\"", "'"));
      }
      escapedMessages.add(escapedMessage);
    }
    return escapedMessages;
  }

  private String escapePrompt(String value) {
    return value.replaceAll("\"", "'");
  }
}
