package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.*;
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
  public OneBuildScripts uiCoreService(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getServices();
  }

  @Bean("uiCoreStores")
  public OneBuildScripts uiCoreStore(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getStores();
  }

  @Bean("uiCoreComponents")
  public OneBuildScripts uiComponents(OneBuildCoreConfigs oneBuildCoreConfigs) {
    return oneBuildCoreConfigs.getComponents();
  }
}