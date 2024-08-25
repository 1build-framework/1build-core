package dev.onebuild.ui.domain.service;

import dev.onebuild.ui.domain.model.config.OneBuildConfigs;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.domain.model.config.UiComponent;
import freemarker.core.InvalidReferenceException;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Map;

import static dev.onebuild.ui.utils.ResourceUtils.readResource;

@Slf4j
public class ScriptService {
  private final OneBuildConfigs oneBuildConfigs;
  private final freemarker.template.Configuration freemarkerConfiguration;
  private final ScriptParameters scriptParameters;

  public ScriptService(OneBuildConfigs oneBuildConfigs,
                       freemarker.template.Configuration freemarkerConfiguration,
                       ScriptParameters scriptParameters) {
    this.oneBuildConfigs = oneBuildConfigs;
    this.freemarkerConfiguration = freemarkerConfiguration;
    this.scriptParameters = scriptParameters;
  }

  public String renderIndex() {
    StringWriter writer = new StringWriter();

    String mainComponentName = oneBuildConfigs.getIndex().getMainComponent();

    UiComponent cp = oneBuildConfigs.getComponents().getList().get(mainComponentName);

    if(cp != null) {
      String importPath = oneBuildConfigs.getComponents().getPath() + "/" + mainComponentName;

      Map<String, Object> model = scriptParameters.getAllParameters();
      model.putAll(Map.of(
          "cssConfig", oneBuildConfigs.getCss(),
          "jsConfig", oneBuildConfigs.getJs(),
          "componentImportPath", importPath
      ));

      try {
        freemarkerConfiguration
            .getTemplate(oneBuildConfigs.getIndex().getTemplate())
            .process(model, writer);
      } catch (Exception e) {
        throw new RuntimeException("Failed to render index", e);
      }
    }

    return writer.toString();
  }

  public String renderComponent(String componentName) {
    StringWriter writer = new StringWriter();

    UiComponent cp = oneBuildConfigs.getComponents().getList().get(componentName);
    String resourcePath = oneBuildConfigs.getComponents().getSourcePath() + "/" +
        cp.getHome();

    String html = readResource(resourcePath, cp.getName() + ".html");
    String css = readResource(resourcePath, cp.getName() + ".css");

    Map<String, Object> model = scriptParameters.getAllParameters();
    model.putAll(Map.of("html", html, "css", css));

    try {
      freemarkerConfiguration
          .getTemplate(cp.getHome() + "/" + cp.getName() + ".js")
          .process(model, writer);
    } catch (Exception e) {
      throw new RuntimeException("Failed to render component", e);
    }

    return writer.toString();
  }

  public String renderService(String serviceName) {
    StringWriter writer = new StringWriter();

    try {
      freemarkerConfiguration
          .getTemplate(serviceName)
          .process(scriptParameters.getAllParameters(), writer);
      return writer.toString();
    }  catch (InvalidReferenceException ire) {
      String missingVariable = ire.getBlamedExpressionString();
      log.error("Error: Missing data '{}' in the file '{}'", missingVariable, serviceName);
    } catch (Exception e) {
      log.error("Error: Unable to process template: ", e);
    }

    return "";
  }
}