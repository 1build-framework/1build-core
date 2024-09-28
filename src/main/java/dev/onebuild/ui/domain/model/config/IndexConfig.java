package dev.onebuild.ui.domain.model.config;

import dev.onebuild.domain.model.ui.OneBuildLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class IndexConfig extends OneBuildLocation {
  private String templateSourcePath;
  private String template;
  private String mainComponent;
}