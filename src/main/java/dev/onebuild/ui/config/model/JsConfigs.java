package dev.onebuild.ui.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "onebuild.ui.js")
public class JsConfigs {
  private String path;
  private String sourcePath;
  private Map<String, String> javascript;
  private Map<String, String> modules;
}