package com.resumechatbot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumechatbot.configs.ChatApiConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatApiPrompt {

  @JsonIgnore // not to be sent to the Chat API
  static final Logger logger = LoggerFactory.getLogger(ChatApiPrompt.class);
  private final String model;
  private final List<Map<String, String>> messages;
  private final float temperature;
  @JsonIgnore // not to be sent to the Chat API
  private String userContent;

  @Autowired
  public ChatApiPrompt(ChatApiConfig chatApiConfig) {
    // TODO extend ChatApiConfig instead?
    this.model = chatApiConfig.getModel();
    this.messages = chatApiConfig.getMessages();
    this.userContent = "";
    this.temperature = chatApiConfig.getTemperature();
  }

  public String getUserContent() {
    return userContent;
  }

  public void setUserContent(String userContent) {
    this.userContent = userContent.replaceAll("\"", "'");
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

  public String toApiRequestBody() {
    // TODO configure - not printed to console (and file?)!
    logger.debug("Chat API model set to \"{}\"", model);
    logger.debug("Chat API system message (master prompt) set to \"{}\"", messages.toString());
    logger.debug("Chat API temperature set to \"{}\"", temperature);

    Map<String, String> userMessage = new HashMap<>();
    userMessage.put("role", "user");
    userMessage.put("content", getUserContent());
    getMessages().add(userMessage);

    String chatApiResponseBody;
    try {
      chatApiResponseBody = new ObjectMapper().writeValueAsString(this); // requires public getters!
    } catch (JsonProcessingException e) {
      logger.error("\"{}\" while mapping chat API prompt to a request body", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return chatApiResponseBody;
  }
}
