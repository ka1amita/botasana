package com.resumechatbot.services;

import com.resumechatbot.dtos.PromptDto;

public interface ChatCompletionService {

  String complete(PromptDto prompt);
}
