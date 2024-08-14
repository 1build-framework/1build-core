package dev.onebuild.testing.html.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiValueData {
  private List<IdValue> values;
  private List<IdValue> selectedValues;
}
