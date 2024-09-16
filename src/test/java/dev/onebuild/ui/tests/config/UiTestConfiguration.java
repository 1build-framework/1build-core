package dev.onebuild.ui.tests.config;

import dev.onebuild.domain.model.OneBuildComponents;
import dev.onebuild.domain.model.OneBuildResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class UiTestConfiguration {
  @Bean("testComponents")
  @ConfigurationProperties(prefix = "onebuild.ui.test.components")
  public OneBuildComponents uiComponents() {
    return new OneBuildComponents();
  }

  @Bean("testServices")
  @ConfigurationProperties(prefix = "onebuild.ui.test.services")
  public OneBuildResources uiServices() {
    return new OneBuildResources();
  }

  @Bean("testStores")
  @ConfigurationProperties(prefix = "onebuild.ui.test.stores")
  public OneBuildResources uiStores() {
    return new OneBuildResources();
  }
}