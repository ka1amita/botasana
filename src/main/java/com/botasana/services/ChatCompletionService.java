package com.botasana.services;

import com.botasana.dtos.PromptDto;

public interface ChatCompletionService {

  String complete(PromptDto prompt);
}
