package dev.onebuild.ui.domain.model.config;


import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OneBuildConfigs {

  @JsonProperty("css")
  private CssConfigs css;

  @JsonProperty("js")
  private JsConfigs js;

  @JsonProperty("components")
  private ComponentsConfigs components;

  @JsonProperty("index")
  private IndexConfigs index;

  @JsonProperty("service")
  private ServiceConfigs service;
}