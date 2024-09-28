package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.OneBuildComponents;
import dev.onebuild.domain.model.ui.OneBuildResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

//@ConditionalOnProperty(value = "onebuild.ui")
//@Configuration
//@EnableConfigurationProperties
public class UiApplicationConfiguration {

  @Bean("appComponents")
  @ConfigurationProperties(prefix = "onebuild.ui.core.components")
  public OneBuildComponents uiComponents() {
    return new OneBuildComponents();
  }

  @Bean("appServices")
  @ConfigurationProperties(prefix = "onebuild.ui.core.services")
  public OneBuildResources uiServices() {
    return new OneBuildResources();
  }

  @Bean("appStores")
  @ConfigurationProperties(prefix = "onebuild.ui.core.stores")
  public OneBuildResources uiStores() {
    return new OneBuildResources();
  }
}