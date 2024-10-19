package dev.onebuild.ui.tests.config;

import dev.onebuild.domain.model.ui.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class UiTestConfiguration {
  @Bean("testComponents")
  @ConfigurationProperties(prefix = "onebuild.ui.test.components")
  public OneBuildScripts uiComponents() {
    return new OneBuildScripts();
  }

}