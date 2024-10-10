package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.OneBuildComponents;
import dev.onebuild.domain.model.ui.OneBuildIndex;
import dev.onebuild.domain.model.ui.OneBuildLocation;
import dev.onebuild.domain.model.ui.OneBuildResources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

  @Bean("uiCoreIndex")
  public OneBuildIndex uiIndex(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getIndex();
  }

  //Not framework but default services and stores
  @Bean("uiCoreServices")
  public OneBuildLocation uiCoreService(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getServices();
  }

  @Bean("uiCoreStores")
  public OneBuildLocation uiCoreStore(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getStores();
  }

  @Bean("uiCoreComponents")
  public OneBuildComponents uiComponents(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getComponents();
  }
}