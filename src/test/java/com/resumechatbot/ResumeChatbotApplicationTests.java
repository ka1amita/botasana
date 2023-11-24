package com.resumechatbot;

import static org.junit.jupiter.api.Assertions.assertNotSame;

import com.resumechatbot.models.ChatApiPrompt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ResumeChatbotApplicationTests {

  @Autowired
  BeanFactory beanFactory;

  @Autowired
  ApplicationContext applicationContext;

  @Test
  void contextLoads() {
  }

  @Test
  void beanFactory_injects_new_instance_of_ChatApiPrompt_every_completion_request() {
    ChatApiPrompt bean1 = beanFactory.getBean(ChatApiPrompt.class);
    ChatApiPrompt bean2 = beanFactory.getBean(ChatApiPrompt.class);
    assertNotSame(bean1, bean2);
    assertNotSame(bean1.getMessages(),
                  bean2.getMessages());
    // fails without `@Scope(value = SCOPE_PROTOTYPE)`
    // but still shared between OpenAI API request!
  }

  @Test
  void context_injects_new_instance_of_ChatApiPrompt_every_completion_request() {
    ChatApiPrompt bean1 = applicationContext.getBean(ChatApiPrompt.class);
    ChatApiPrompt bean2 = applicationContext.getBean(ChatApiPrompt.class);
    assertNotSame(bean1, bean2);
    assertNotSame(bean1.getMessages(), bean2.getMessages());
    // fails without `@Scope(value = SCOPE_PROTOTYPE)`
    // but still shared between OpenAI API request!
  }
}
