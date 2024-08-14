package dev.onebuild.ui.config.model;

public enum ResourceName {
  DEFAULT("default"),
  MATERIAL("material"),
  VUE("vue"),
  VUE_ROUTER("vue-router"),
  VUETIFY("vuetify"),
  VUETIFY_ESM("vuetify-esm");

  private final String name;

  ResourceName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  public static ResourceName fromName(String name) {
    for (ResourceName resourceName : ResourceName.values()) {
      if (resourceName.getName().equals(name)) {
        return resourceName;
      }
    }
    return null;
  }
}
