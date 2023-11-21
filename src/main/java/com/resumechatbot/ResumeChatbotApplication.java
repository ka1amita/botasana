package com.resumechatbot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan // because of ConfigurationProperties
@SpringBootApplication
public class ResumeChatbotApplication implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(ResumeChatbotApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
  }
}
