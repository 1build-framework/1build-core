package dev.onebuild.ui.config;

import dev.onebuild.domain.model.OneBuildComponents;
import dev.onebuild.domain.model.OneBuildIndex;
import dev.onebuild.domain.model.OneBuildLocation;
import dev.onebuild.domain.model.OneBuildResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class UiPropertiesConfiguration {
  @Bean("uiCoreJsResources")
  public OneBuildResources uiCoreJsResources(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getJs();
  }

  @Bean("uiCoreCssResources")
  public OneBuildResources uiCoreCssResources(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getCss();
  }

  @Bean
  public OneBuildIndex uiIndex(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getIndex();
  }

  //Not framework but default services and stores
  @Bean("uiCoreService")
  public OneBuildLocation uiCoreService(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getServices();
  }

  @Bean("uiCoreStore")
  public OneBuildLocation uiCoreStore(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getStores();
  }

  @Bean
  public OneBuildComponents uiComponents(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getComponents();
  }
}