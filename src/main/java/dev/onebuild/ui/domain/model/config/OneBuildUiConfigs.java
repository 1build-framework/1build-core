package dev.onebuild.ui.domain.model.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "onebuild.ui")
public class OneBuildUiConfigs {

  private CssConfig css;

  private JsConfig js;

  private ComponentsConfig component;

  private StoreConfig store;

  private IndexConfig index;

  private ServiceConfig service;

  private boolean prodEnabled = false;
}