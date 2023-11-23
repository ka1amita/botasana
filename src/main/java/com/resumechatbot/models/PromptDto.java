package com.resumechatbot.models;

import com.resumechatbot.utils.ClientPrompt;

public class PromptDto {

  @ClientPrompt
  private String prompt;

  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }
}