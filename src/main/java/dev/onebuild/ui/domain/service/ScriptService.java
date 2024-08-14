package dev.onebuild.ui.domain.service;

import dev.onebuild.ui.config.model.*;

import java.io.StringWriter;
import java.util.Map;

import static dev.onebuild.ui.utils.ResourceUtils.readResource;

public class ScriptService {
  private final CssConfigs cssConfigs;
  private final JsConfigs jsConfigs;
  private final ComponentsConfigs componentsConfigs;
  private final IndexConfigs indexConfigs;
  private final freemarker.template.Configuration freemarkerConfiguration;

  public ScriptService(CssConfigs cssConfigs,
                       JsConfigs jsConfigs,
                       ComponentsConfigs componentsConfigs,
                       IndexConfigs indexConfigs,
                       freemarker.template.Configuration freemarkerConfiguration) {
    this.cssConfigs = cssConfigs;
    this.jsConfigs = jsConfigs;
    this.componentsConfigs = componentsConfigs;
    this.indexConfigs = indexConfigs;
    this.freemarkerConfiguration = freemarkerConfiguration;
  }

  public String renderIndex() {
    StringWriter writer = new StringWriter();

    String mainComponentName = indexConfigs.getMainComponent();

    ComponentConfigs cp = componentsConfigs.getList().get(mainComponentName);

    /*Optional<Map.Entry<String, ComponentConfigs>> cp = componentsConfigs.getList()
        .entrySet()
        .stream()
        .filter((e) -> e.getValue().isMain())
        .findFirst();*/

    if(cp != null) {
      String importPath = componentsConfigs.getPath() + "/" + mainComponentName;

      Map<String, Object> model = Map.of(
          "cssConfig", cssConfigs,
          "jsConfig", jsConfigs,
          "componentImportPath", importPath
      );

      try {
        freemarkerConfiguration
            .getTemplate(indexConfigs.getTemplate())
            .process(model, writer);
      } catch (Exception e) {
        throw new RuntimeException("Failed to render index", e);
      }
    }

    return writer.toString();
  }

  public String renderComponent(String componentName) {
    StringWriter writer = new StringWriter();

    ComponentConfigs cp = componentsConfigs.getList().get(componentName);
    String resourcePath = componentsConfigs.getSourcePath() + "/" +
        cp.getHome();

    String html = readResource(resourcePath, cp.getName() + ".html");
    String css = readResource(resourcePath, cp.getName() + ".css");

    Map<String, Object> model = Map.of(
        "html", html,
        "css", css);

    try {
      freemarkerConfiguration
          .getTemplate(cp.getHome() + "/" + cp.getName() + ".js")
          .process(model, writer);
    } catch (Exception e) {
      throw new RuntimeException("Failed to render component", e);
    }

    return writer.toString();
  }
}