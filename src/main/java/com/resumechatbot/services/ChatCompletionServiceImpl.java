package com.resumechatbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumechatbot.configs.ChatApiConfig;
import com.resumechatbot.models.ChatApiPrompt;
import java.util.Collections;
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
  private final ChatApiConfig chatApiConfig;
  private final String openaiApiKey;

  @Autowired
  public ChatCompletionServiceImpl(
      @Value(value = "${openai.api.key}") String openaiApiKey,
      ChatApiConfig chatApiConfig) {
    this.openaiApiKey = openaiApiKey;
    this.chatApiConfig = chatApiConfig;
  }

  @Override
  public String complete(String userContent) {
    // also logged by `o.s.web.client.RestTemplate` logger
    logger.info("{} has accepted a prompt: \"{}\" for completion",
                this.getClass().getSimpleName(),
                userContent);

    ChatApiPrompt chatApiPrompt = new ChatApiPrompt(chatApiConfig); // must be new! TODO add test to check
    chatApiPrompt.setUserContent(userContent);

    ResponseEntity<String> chatApiResponse = sendChatApiRequest(chatApiPrompt);
    String completion = acceptChatApiResponse(chatApiResponse);

    // also logged by `o.s.w.s.m.m.a.HttpEntityMethodProcessor` logger
    logger.info("{} has completed: \"{}\"", this.getClass().getSimpleName(), completion);

    return completion;
  }

  @NotNull
  private ResponseEntity<String> sendChatApiRequest(ChatApiPrompt prompt) {
    // https://stackoverflow.com/questions/67984754/how-to-perform-an-http-request-to-another-server-from-spring-boot-controller
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(openaiApiKey);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> chatApiRequest = new HttpEntity<>(prompt.toApiRequestBody(), headers);

    return new RestTemplate().postForEntity(CHAT_API_URL,
                                          chatApiRequest,
                                          String.class);
  }

  private String acceptChatApiResponse(ResponseEntity<String> chatApiResponse) {
    String chatApiResponseBody = chatApiResponse.getBody();
    JsonNode tree;
    try {
      tree = new ObjectMapper().readTree(chatApiResponseBody);
    } catch (JsonProcessingException e) {
      logger.error("\"{}\" while mapping chat API response body", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return tree.get("choices").get(0).get("message").get("content").asText();
  }
}
