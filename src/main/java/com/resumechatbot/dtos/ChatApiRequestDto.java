package com.resumechatbot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatApiRequestDto {

  @JsonIgnore // not to be sent to the Chat API
  static final Logger logger = LoggerFactory.getLogger(ChatApiRequestDto.class);
  private final String model;
  private final List<Map<String, String>> messages;
  private final float temperature;

  public ChatApiRequestDto(String model, List<Map<String, String>> messages, float temperature) {
    this.model = model;
    this.messages = List.copyOf(messages);
    this.temperature = temperature;

    logger.debug("chat API request model set to \"{}\"", model);
    logger.debug("chat API request messages set to \"{}\"", messages);
    logger.debug("chat API request temperature set to \"{}\"", temperature);
  }

  public String getModel() {
    return model;
  }

  public List<Map<String, String>> getMessages() {
    return messages;
  }

  public float getTemperature() {
    return temperature;
  }

  public String toApiRequestBody() {
    String chatApiResponseBody;
    try {
      chatApiResponseBody = new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      logger.error("\"{}\" while serializing instance to string", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return chatApiResponseBody;
  }
}
