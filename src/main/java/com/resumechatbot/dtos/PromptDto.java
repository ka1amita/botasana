package com.resumechatbot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resumechatbot.configs.ChatApiConfig;
import com.resumechatbot.utils.ClientPrompt;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromptDto {

  @JsonIgnore // not to be read or serialized
  static final Logger logger = LoggerFactory.getLogger(PromptDto.class);
  private String model;
  private List<Map<String, String>> messages;
  private float temperature;
  @ClientPrompt(message = "{error.client_prompt}")
  private String prompt;

  public PromptDto() {
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<Map<String, String>> getMessages() {
    return messages;
  }

  public void setMessages(List<Map<String, String>> messages) {
    this.messages = messages;
  }

  public float getTemperature() {
    return temperature;
  }

  public void setTemperature(float temperature) {
    this.temperature = temperature;
  }

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    // also logged by `o.s.web.client.RestTemplate` logger
    logger.info("user prompt: \"{}\" accepted", prompt);

    this.prompt = prompt.replaceAll("\"", "'");

    logger.debug("user prompt: \"{}\" set", getPrompt());
  }

  public void setSettingMessages(ChatApiConfig chatApiConfig) {
    setModel(chatApiConfig.getModel());
    setMessages(new ArrayList<>(chatApiConfig.getMessages())); // chatApiConfig's list is immutable!
    setTemperature(chatApiConfig.getTemperature());
    logger.debug("prompt model set to \"{}\"", getModel());
    logger.debug("prompt messages set to \"{}\"", getMessages());
    logger.debug("prompt temperature set to \"{}\"", getTemperature());
  }
}
