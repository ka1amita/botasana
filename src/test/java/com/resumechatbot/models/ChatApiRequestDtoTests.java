package com.resumechatbot.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumechatbot.dtos.ChatApiRequestDto;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.Test;

class ChatApiRequestDtoTests {

  @Test
  void prompt_has_only_the_correct_nodes() throws JsonProcessingException {
    Set<String> fields = new HashSet<>();
    fields.add("model");
    fields.add("messages");
    fields.add("temperature");
    ChatApiRequestDto chatApiRequestDto = new ChatApiRequestDto("model", new ArrayList<>(), 0);
    String chatApiResponseBody = chatApiRequestDto.toApiRequestBody();
    JsonNode jsonNode = new ObjectMapper().readTree(chatApiResponseBody);

    assertTrue(fields.containsAll(Streams.stream(jsonNode.fields()).map(Entry::getKey).toList()));
    assertEquals(fields.size(), jsonNode.size());
  }
}
