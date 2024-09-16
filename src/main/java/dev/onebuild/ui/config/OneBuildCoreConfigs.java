package dev.onebuild.ui.config;

import dev.onebuild.domain.model.OneBuildComponents;
import dev.onebuild.domain.model.OneBuildIndex;
import dev.onebuild.domain.model.OneBuildResources;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneBuildCoreConfigs {

  private OneBuildResources js;
  private OneBuildResources css;

  private OneBuildIndex index;

  private OneBuildResources services;

  private OneBuildResources stores;

  public OneBuildComponents components;
}
