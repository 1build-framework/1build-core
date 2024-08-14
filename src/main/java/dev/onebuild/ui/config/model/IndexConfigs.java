package dev.onebuild.ui.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "onebuild.ui.index")
public class IndexConfigs {
  private String path;
  private String sourcePath;
  private String templateSourcePath;
  private String template;

  private String mainComponent;
}