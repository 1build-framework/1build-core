package dev.onebuild.core.config;

import dev.onebuild.commons.domain.model.ui.OneBuildScripts;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class UiApplicationConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "onebuild.ui.core.components")
  public OneBuildScripts uiComponents() {
    return new OneBuildScripts();
  }

  @Bean
  @ConfigurationProperties(prefix = "onebuild.ui.core.services")
  public OneBuildScripts uiServices() {
    return new OneBuildScripts();
  }

  @Bean
  @ConfigurationProperties(prefix = "onebuild.ui.core.stores")
  public OneBuildScripts uiStores() {
    return new OneBuildScripts();
  }
}