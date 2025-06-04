package dev.onebuild.core.domain.service;

import dev.onebuild.commons.domain.model.OneBuildAppSettings;
import dev.onebuild.commons.domain.model.ui.*;
import dev.onebuild.core.domain.model.config.ScriptParameters;
import dev.onebuild.commons.errors.OneBuildExceptionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.onebuild.core.utils.AppUtils.processPagelets;
import static dev.onebuild.core.utils.ResourceUtils.sortResources;

@Slf4j
public class ScriptService {
  private final String validatorTemplate = "validator.js.ftl";
  private final OneBuildExceptionFactory exceptionFactory;
  private final OneBuildAppSettings oneBuildAppSettings;
  private final OneBuildIndex oneBuildIndex;
  private final OneBuildValidator oneBuildValidator;
  private final List<OneBuildLocation> locations;
  private final freemarker.template.Configuration uiFreemarkerConfiguration;
  private final ScriptParameters scriptParameters;

  public ScriptService(OneBuildExceptionFactory exceptionFactory,
                       OneBuildAppSettings oneBuildAppSettings,
                       OneBuildIndex oneBuildIndex,
                       OneBuildValidator oneBuildValidator,
                       List<OneBuildLocation> locations,
                       freemarker.template.Configuration uiFreemarkerConfiguration,
                       ScriptParameters scriptParameters) {
    this.exceptionFactory = exceptionFactory;
    this.oneBuildAppSettings = oneBuildAppSettings;
    this.oneBuildIndex = oneBuildIndex;
    this.oneBuildValidator = oneBuildValidator;
    this.locations = locations;
    this.uiFreemarkerConfiguration = uiFreemarkerConfiguration;
    this.scriptParameters = scriptParameters;
  }

  public String renderIndex() {
    StringWriter writer = new StringWriter();

    OneBuildScripts oneBuildComponents = locations.stream()
        .filter(location -> location.getResourceType() == ResourceType.SCRIPT)
        .map(location -> (OneBuildScripts) location)
        .filter(component -> component.getList() != null)
        .filter(component -> component.getList().stream().anyMatch(uiComponent -> uiComponent.getName().equals(oneBuildAppSettings.getMainComponent())))
        .findFirst()
        .orElse(null);

    UiScript cp = oneBuildComponents.getList().stream()
        .filter(c -> c.getName().equalsIgnoreCase(oneBuildAppSettings.getMainComponent()))
        .findFirst()
        .orElse(null);

    if(cp != null) {
      String importPath = oneBuildComponents.getWebPath() + cp.getHome();

      Set<String> cssResources = locations.stream()
          .filter(location -> location.getResourceType() == ResourceType.CSS)
          .map(location -> (OneBuildResources) location)
          .flatMap(resource -> resource.getResources().stream().map(res -> resource.getWebPath() + "/" + res))
          .collect(Collectors.toSet());

      List<String> jsResources = locations.stream()
          .filter(location -> location.getResourceType() == ResourceType.JS)
          .map(location -> (OneBuildResources) location)
          .flatMap(resource -> resource.getResources().stream().map(res -> resource.getWebPath() + "/" + res))
          .collect(Collectors.toList());
      sortResources(jsResources);

      Map<String, Object> model = scriptParameters.getAllParameters();
      model.putAll(Map.of(
          "cssResources", cssResources,
          "jsResources", jsResources,
          "componentImportPath", importPath
      ));

      try {
        uiFreemarkerConfiguration
            .getTemplate(oneBuildIndex.getTemplate())
            .process(model, writer);
      } catch (Exception e) {
        throw new RuntimeException("Failed to render index", e);
      }
    }
    return writer.toString();
  }

  public String renderScript(String contextPath, String scriptName) {
    StringWriter writer = new StringWriter();

    OneBuildScripts oneBuildScripts = locations.stream()
        .filter(location -> location.getResourceType() == ResourceType.SCRIPT)
        .filter(location -> location.getWebPath().equals(contextPath))
        .map(location -> (OneBuildScripts) location)
        .filter(component -> component.getList() != null)
        .filter(component -> component.getList().stream().anyMatch(uiComponent -> uiComponent.getName().equals(scriptName)))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createComponentNotFoundException(contextPath + "/" + scriptName));

    UiScript script = oneBuildScripts.getList().stream()
        .filter(c -> c.getName().equalsIgnoreCase(scriptName))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createComponentNotFoundException(scriptName));

    Map<String, Object> model = processPagelets(oneBuildScripts.getSourcePath(), script);
    try {
      uiFreemarkerConfiguration
          .getTemplate(script.getHome() + "/" + script.getResource())
          .process(model, writer);
    } catch (Exception e) {
      throw new RuntimeException("Failed to render component. " + (oneBuildScripts.getSourcePath() + script.getHome() + "/" + script.getResource()), e);
    }

    return writer.toString();
  }

  public String renderValidator() {
    if(oneBuildValidator == null) {
      log.warn("No validator configuration was found.");
      return "";
    }

    StringWriter writer = new StringWriter();
    try {
      uiFreemarkerConfiguration
          .getTemplate(validatorTemplate)
          .process(Collections.singletonMap("validators", oneBuildValidator.getList()), writer);
    } catch (Exception e) {
      throw new RuntimeException("Failed to render validators.", e);
    }

    return writer.toString();
  }
}