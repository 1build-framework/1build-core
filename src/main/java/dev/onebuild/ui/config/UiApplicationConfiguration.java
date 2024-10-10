package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.OneBuildComponents;
import dev.onebuild.domain.model.ui.OneBuildResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class UiApplicationConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "onebuild.ui.core.components")
  public OneBuildComponents uiComponents() {
    return new OneBuildComponents();
  }

  @Bean
  @ConfigurationProperties(prefix = "onebuild.ui.core.services")
  public OneBuildResources uiServices() {
    return new OneBuildResources();
  }

  @Bean
  @ConfigurationProperties(prefix = "onebuild.ui.core.stores")
  public OneBuildResources uiStores() {
    return new OneBuildResources();
  }
}