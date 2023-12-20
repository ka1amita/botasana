package com.botasana.configs;

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();

    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean //for Validation annotations like @Email
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

  @Bean
  public LocaleResolver localeResolver() {
    // interestingly enough this default (if e.g. set to "cz") is overridden with english if
    // the "Accept-Language" header is empty, but not if it is missing nor if it is set
    // to something silly; the message.properties files change nothing about it.
    List<Locale> supportedLocales = List.of(new Locale("cz"));

    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    // if the language is missing, then it really doesn't localize it into;
    // only english is supported despite that
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    localeResolver.setSupportedLocales(supportedLocales);
    return localeResolver;
  }
}