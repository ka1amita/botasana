package com.resumechatbot.dtos;

import com.resumechatbot.utils.ClientPrompt;

public class PromptDto {

  @ClientPrompt(message = "{error.client_prompt}")
  private String prompt;

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }
}