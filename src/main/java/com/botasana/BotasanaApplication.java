package com.botasana;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationPropertiesScan // because of ConfigurationProperties
@SpringBootApplication
public class BotasanaApplication {

  private final String serverPort;

  public BotasanaApplication(@Value(value = "${server.port}") String serverPort) {
    this.serverPort = serverPort;
  }

  public static void main(String[] args) {
    SpringApplication.run(BotasanaApplication.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:" + serverPort);
      }
    };
  }
}
