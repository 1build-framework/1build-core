package dev.onebuild.ui.domain.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsConfig {
  private String path;
  private String sourcePath;
  private Map<String, String> javascript;
  private Map<String, String> modules;
}