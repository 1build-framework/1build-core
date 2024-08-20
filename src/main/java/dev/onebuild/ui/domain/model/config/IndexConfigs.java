package dev.onebuild.ui.domain.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexConfigs {
  private String path;
  private String sourcePath;
  private String templateSourcePath;
  private String template;

  private String mainComponent;
}