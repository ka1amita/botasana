package com.botasana;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan // because of ConfigurationProperties
@SpringBootApplication
public class BotasanaApplication implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(BotasanaApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
  }
}
