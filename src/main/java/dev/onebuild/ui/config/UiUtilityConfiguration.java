package dev.onebuild.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiUtilityConfiguration {

  @Bean("mapper")
  public ObjectMapper mapper() {
    return new ObjectMapper();
  }
}
