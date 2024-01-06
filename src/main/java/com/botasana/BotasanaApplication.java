package com.botasana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan // because of ConfigurationProperties
@SpringBootApplication
public class BotasanaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BotasanaApplication.class, args);
  }
}
