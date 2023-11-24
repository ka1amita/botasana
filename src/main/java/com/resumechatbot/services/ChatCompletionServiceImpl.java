package com.resumechatbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumechatbot.configs.ChatApiConfig;
import com.resumechatbot.dtos.ChatApiRequestDto;
import com.resumechatbot.dtos.PromptDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatCompletionServiceImpl implements ChatCompletionService {

  static final Logger logger = LoggerFactory.getLogger(ChatCompletionServiceImpl.class);
  private static final String CHAT_API_URL = "https://api.openai.com/v1/chat/completions";
  private final String openaiApiKey;
  private final RestTemplate restTemplate;
  private final ChatApiConfig chatApiConfig;

  @Autowired
  public ChatCompletionServiceImpl(
      @Value(value = "${openai.api.key}") String openaiApiKey,
      ChatApiConfig chatApiConfig, RestTemplate restTemplate) {
    this.openaiApiKey = openaiApiKey;
    this.restTemplate = restTemplate;
    this.chatApiConfig = chatApiConfig;
  }

  @Override
  public String complete(PromptDto promptDto) {
    promptDto.setSettingMessages(chatApiConfig);

    ChatApiRequestDto chatApiRequestDto = mapChatApiRequestDto(promptDto);

    ResponseEntity<String> chatApiResponse = sendChatApiRequest(chatApiRequestDto);
    String completion = acceptChatApiResponse(chatApiResponse);

    // also logged by `o.s.w.s.m.m.a.HttpEntityMethodProcessor` logger
    logger.info("{} has completed: \"{}\"", this.getClass().getSimpleName(), completion);

    return completion;
  }

  @NotNull
  private ResponseEntity<String> sendChatApiRequest(ChatApiRequestDto chatApiRequestDto) {
    // https://stackoverflow.com/questions/67984754/how-to-perform-an-http-request-to-another-server-from-spring-boot-controller
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(openaiApiKey);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> chatApiRequest =
        new HttpEntity<>(chatApiRequestDto.toApiRequestBody(), headers);

    return restTemplate.postForEntity(CHAT_API_URL,
                                      chatApiRequest,
                                      String.class);
  }

  private String acceptChatApiResponse(ResponseEntity<String> chatApiResponse) {
    String chatApiResponseBody = chatApiResponse.getBody();
    JsonNode tree;
    try {
      tree = new ObjectMapper().readTree(chatApiResponseBody);
    } catch (JsonProcessingException e) {
      logger.error("\"{}\" while mapping response to a completion string", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return tree.get("choices").get(0).get("message").get("content").asText();
  }

  public ChatApiRequestDto mapChatApiRequestDto(PromptDto promptDto) {

    Map<String, String> userMessage = new HashMap<>();
    List<Map<String, String>> userMessages = new ArrayList<>(promptDto.getMessages());
    userMessage.put("role", "user");
    userMessage.put("content", promptDto.getPrompt());
    userMessages.add(userMessage);

    return new ChatApiRequestDto(promptDto.getModel(), userMessages, promptDto.getTemperature());
  }
}
