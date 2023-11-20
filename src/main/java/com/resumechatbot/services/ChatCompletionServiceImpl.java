package com.resumechatbot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private String openaiApiKey; // final modifier breaks unit ChatCompletionServiceImplUnitTests
  private float chatTemperature; // final modifier breaks unit ChatCompletionServiceImplUnitTests
  private RestTemplate restTemplate; // final modifier breaks unit ChatCompletionServiceImplUnitTests

  public ChatCompletionServiceImpl() {
    // TODO try to get rid of the default constructor without breaking the tests
    // added to satisfy @InjectMocks in ChatCompletionServiceImplUnitTests
  }

  @Autowired
  public ChatCompletionServiceImpl(
      @Value(value = "${openai.api.key}") String openaiApiKey,
      RestTemplate restTemplate,
      @Value(value = "${openai.api.chat.temperature}") float chatTemperature) {
    this.openaiApiKey = openaiApiKey;
    this.restTemplate = restTemplate;
    this.chatTemperature = chatTemperature;
  }

  @Override
  public String complete(String prompt) {
    logger.info("{} has accepted a prompt: \"{}\" for completion",
                this.getClass().getSimpleName(),
                prompt);

    ResponseEntity<String> chatApiResponse = sendChatApiRequest(prompt);
    String completion = acceptChatApiResponse(chatApiResponse);
    logger.info("{} has completed: \"{}\"", this.getClass().getSimpleName(), completion);
    return completion;
  }

  private String prepareChatApiRequestBody(String prompt) {
    // TODO add completion length limit to the api request
    String chatApiRequestBodyTemplate = """
        {
          "model": "gpt-3.5-turbo-1106",
          "messages": [{"role": "user", "content": "%1$s"}],
          "temperature": %2$.2f
        }
        """;

    logger.debug("Chat API template set to \"{}\"", chatApiRequestBodyTemplate);
    logger.debug("Chat API temperature set to \"{}\"", chatTemperature);

    return String.format(chatApiRequestBodyTemplate,
                         // `"` has to be quoted otherwise break the JSON body
                         prompt.replaceAll("\"", "'"), chatTemperature);
  }

  @NotNull
  private ResponseEntity<String> sendChatApiRequest(String prompt) {
    // https://stackoverflow.com/questions/67984754/how-to-perform-an-http-request-to-another-server-from-spring-boot-controller
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(openaiApiKey);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    String chatApiRequestBody = prepareChatApiRequestBody(prompt);

    HttpEntity<String> chatApiRequest = new HttpEntity<>(chatApiRequestBody, headers);

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
      logger.error("\"{}\" while mapping chat API response body", e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return tree.get("choices").get(0).get("message").get("content").asText();
  }
}
