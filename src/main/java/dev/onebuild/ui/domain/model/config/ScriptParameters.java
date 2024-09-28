package dev.onebuild.ui.domain.model.config;

import java.util.Map;
import java.util.HashMap;

public class ScriptParameters {
  private Map<String, Object> parameters;
  public ScriptParameters() {
    this.parameters = new HashMap<>();
  }
  public void addParameter(String key, Object value) {
    parameters.put(key, value);
  }
  public void removeParameter(String key) {
    parameters.remove(key);
  }
  public Object getParameter(String key) {
    return parameters.get(key);
  }
  public Map<String, Object> getAllParameters() {
    return new HashMap<>(parameters);
  }
}