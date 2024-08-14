package dev.onebuild.testing.html.api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = { "id", "value" })
public class IdValue {
  private String id;
  private String value;
}