package dev.onebuild.ui.config;

import dev.onebuild.domain.model.ui.OneBuildComponents;
import dev.onebuild.domain.model.ui.OneBuildIndex;
import dev.onebuild.domain.model.ui.OneBuildLocation;
import dev.onebuild.domain.model.ui.OneBuildResources;
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

  private OneBuildLocation services;
  private OneBuildLocation stores;

  public OneBuildComponents components;
}