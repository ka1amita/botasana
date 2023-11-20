package com.resumechatbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {

  static final Logger logger = LoggerFactory.getLogger(ChatGptService.class);

  @Value(value = "${openai.api.key}")
  private String openaiApiKey;

/*
  * curl https://api.openai.com/v1/chat/completions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -d '{
     "model": "gpt-3.5-turbo-1106",
     "messages": [{"role": "user", "content": "Say this is a test!"}],
     "temperature": 0.7
   }'
   */

  /*
{
  "id": "chatcmpl-8MvPspPX80R2MSfBu7owaW3GPXGqZ",
  "object": "chat.completion",
  "created": 1700474616,
  "model": "gpt-3.5-turbo-1106",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "This is a test!"
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 13,
    "completion_tokens": 5,
    "total_tokens": 18
  },
  "system_fingerprint": "fp_eeff13170a"
}
*/
  public String complete(String prompt) {
    logger.info("{} has accepted a prompt: \"{}\"", this.getClass().getSimpleName(), prompt);
    // https://stackoverflow.com/questions/67984754/how-to-perform-an-http-request-to-another-server-from-spring-boot-controller
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(openaiApiKey);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    String chatApiRequestBodyTemplate = """
        {
          "model": "gpt-3.5-turbo-1106",
          "messages": [{"role": "user", "content": "%s"}],
          "temperature": 0.7
        }
        """;
    String chatApiRequestBody = String.format(chatApiRequestBodyTemplate, prompt);
    HttpEntity<String> chatApiRequest = new HttpEntity<>(chatApiRequestBody, headers);

    ResponseEntity<String> chatApiResponse = new RestTemplate().postForEntity(
        "https://api.openai.com/v1/chat/completions",
        chatApiRequest,
        String.class);

    String chatApiResponseBody = chatApiResponse.getBody();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode tree;
    try {
      tree = mapper.readTree(chatApiResponseBody);
    } catch (JsonProcessingException e) {
      logger.error("\"{}\" while mapping chat API response body", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    String completion = tree.get("choices").get(0).get("message").get("content").asText();
    logger.info("{} has completed: \"{}\"", this.getClass().getSimpleName(), completion);
    return completion;
  }
}
