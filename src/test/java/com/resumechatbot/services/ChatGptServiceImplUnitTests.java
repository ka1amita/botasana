package com.resumechatbot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ChatGptServiceImplUnitTests {

  @Mock
  RestTemplate restTemplate;

  @InjectMocks
  ChatGptServiceImpl chatGptServiceImpl;

  @Test
  public void complete_returns_doublequoted_content_from_chat_api_response() {
    // arrange
    String responseBody =
        """
            {
                "id": "chatcmpl-abc123",
                "object": "chat.completion",
                "created": 1677858242,
                "model": "gpt-3.5-turbo-1106",
                "usage": {
                    "prompt_tokens": 13,
                    "completion_tokens": 7,
                    "total_tokens": 20
                },
                "choices": [
                    {
                        "message": {
                            "role": "assistant",
                            "content": "%s"
                        },
                        "finish_reason": "stop",
                        "index": 0
                    }
                ]
            }
            """;
    // TODO test all unicode characters
    String expected = "\\\"quoted\\\"";
    ResponseEntity<String> response = new ResponseEntity<>(String.format(responseBody, expected),
                                                           HttpStatusCode.valueOf(200));
    when(restTemplate.postForEntity(any(String.class),
                                    any(HttpEntity.class),
                                    eq(String.class)))
        .thenReturn(response);
    // act
    String actual = chatGptServiceImpl.complete("any");
    // assert
    assertEquals(expected.translateEscapes(), actual);
  }

  @Test
  public void complete_returns_word_character_content_from_chat_api_response() {
    // arrange
    String responseBody =
        """
            {
                "id": "chatcmpl-abc123",
                "object": "chat.completion",
                "created": 1677858242,
                "model": "gpt-3.5-turbo-1106",
                "usage": {
                    "prompt_tokens": 13,
                    "completion_tokens": 7,
                    "total_tokens": 20
                },
                "choices": [
                    {
                        "message": {
                            "role": "assistant",
                            "content": "%s"
                        },
                        "finish_reason": "stop",
                        "index": 0
                    }
                ]
            }
            """;
    // TODO test all unicode characters
    String expected = "_aA0";
    ResponseEntity<String> response = new ResponseEntity<>(String.format(responseBody, expected),
                                                           HttpStatusCode.valueOf(200));
    when(restTemplate.postForEntity(any(String.class),
                                    any(HttpEntity.class),
                                    eq(String.class)))
        .thenReturn(response);
    // act
    String actual = chatGptServiceImpl.complete("any");
    // assert
    assertEquals(expected, actual);
  }

  @Test
  public void complete_returns_letter_character_content_from_chat_api_response() {
    // arrange
    String responseBody =
        """
            {
                "id": "chatcmpl-abc123",
                "object": "chat.completion",
                "created": 1677858242,
                "model": "gpt-3.5-turbo-1106",
                "usage": {
                    "prompt_tokens": 13,
                    "completion_tokens": 7,
                    "total_tokens": 20
                },
                "choices": [
                    {
                        "message": {
                            "role": "assistant",
                            "content": "%s"
                        },
                        "finish_reason": "stop",
                        "index": 0
                    }
                ]
            }
            """;
    String expected = "Test";
    ResponseEntity<String> response = new ResponseEntity<>(String.format(responseBody, expected),
                                                           HttpStatusCode.valueOf(200));
    when(restTemplate.postForEntity(any(String.class),
                                    any(HttpEntity.class),
                                    eq(String.class)))
        .thenReturn(response);
    // act
    String actual = chatGptServiceImpl.complete("any");
    // assert
    assertEquals(expected, actual);
  }
}