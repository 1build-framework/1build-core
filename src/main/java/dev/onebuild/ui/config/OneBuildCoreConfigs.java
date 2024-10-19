package dev.onebuild.ui.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.onebuild.domain.model.ui.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneBuildCoreConfigs {

  private OneBuildResources js;

  private OneBuildResources css;

  private OneBuildIndex index;

  private OneBuildScripts services;

  private OneBuildScripts stores;

  private OneBuildScripts components;
}