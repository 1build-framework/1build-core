package dev.onebuild.ui.domain.model.config;

import dev.onebuild.domain.model.OneBuildLocation;
import dev.onebuild.domain.model.UiComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class ComponentsConfig extends OneBuildLocation {
  private List<UiComponent> list = new ArrayList<>();
}