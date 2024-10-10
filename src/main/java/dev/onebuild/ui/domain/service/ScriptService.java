package dev.onebuild.ui.domain.service;

import dev.onebuild.domain.model.*;
import dev.onebuild.domain.model.ui.*;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.errors.OneBuildExceptionFactory;
import freemarker.core.InvalidReferenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.onebuild.ui.utils.AppUtils.toJson;
import static dev.onebuild.ui.utils.ResourceUtils.readResource;
import static dev.onebuild.ui.utils.ResourceUtils.sortResources;

@Slf4j
public class ScriptService {
  private final OneBuildExceptionFactory exceptionFactory;
  private final OneBuildAppSettings oneBuildAppSettings;
  private final OneBuildIndex oneBuildIndex;
  private final List<OneBuildResources> resources;
  private final List<OneBuildComponents> components;
  private final freemarker.template.Configuration uiFreemarkerConfiguration;
  private final ScriptParameters scriptParameters;
  private final ApplicationContext applicationContext;

  public ScriptService(OneBuildExceptionFactory exceptionFactory,
                       OneBuildAppSettings oneBuildAppSettings,
                       OneBuildIndex oneBuildIndex,
                       List<OneBuildResources> resources,
                       List<OneBuildComponents> components,
                       freemarker.template.Configuration uiFreemarkerConfiguration,
                       ScriptParameters scriptParameters,
                       ApplicationContext applicationContext) {
    this.exceptionFactory = exceptionFactory;
    this.oneBuildAppSettings = oneBuildAppSettings;
    this.oneBuildIndex = oneBuildIndex;
    this.resources = resources;
    this.components = components;
    this.uiFreemarkerConfiguration = uiFreemarkerConfiguration;
    this.scriptParameters = scriptParameters;
    this.applicationContext = applicationContext;
  }

  public String renderIndex() {
    StringWriter writer = new StringWriter();

    OneBuildComponents oneBuildComponents = components.stream()
        .filter(component -> component.getList().stream()
            .anyMatch(uiComponent -> uiComponent.getName().equals(oneBuildAppSettings.getMainComponent())))
        .findFirst()
        .orElse(null);

    UiComponent cp = oneBuildComponents.getList().stream()
        .filter(c -> c.getName().equalsIgnoreCase(oneBuildAppSettings.getMainComponent()))
        .findFirst()
        .orElse(null);

    if(cp != null) {
      String importPath = oneBuildComponents.getPath() + cp.getHome();

      Set<String> cssResources = resources.stream()
          .filter(resource -> resource.getResourceType() == ResourceType.CSS)
          .flatMap(resource -> resource.getResources().stream()
              .map(res -> resource.getPath() + "/" + res))
          .collect(Collectors.toSet());

      List<String> jsResources = resources.stream()
          .filter(resource -> resource.getResourceType() == ResourceType.JS)
          .flatMap(resource -> resource.getResources().stream()
              .map(res -> resource.getPath() + "/" + res))
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

  public String renderComponent(String componentName) {
    StringWriter writer = new StringWriter();

    OneBuildComponents oneBuildComponents = components.stream()
        .filter(component -> component.getList().stream()
            .anyMatch(uiComponent -> uiComponent.getName().equals(componentName)))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createComponentNotFoundException(componentName));

    UiComponent cp = oneBuildComponents.getList().stream()
        .filter(c -> c.getName().equalsIgnoreCase(componentName))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createComponentNotFoundException(componentName));

    String resourcePath = oneBuildComponents.getSourcePath() + cp.getHome();

    Map<String, Object> model = scriptParameters.getAllParameters();

    //Merge html and css
    String html = readResource(true, resourcePath, componentName + ".html");
    String css = readResource(true, resourcePath, componentName + ".css");
    model.putAll(Map.of("html", html, "css", css));

    try {
      uiFreemarkerConfiguration
          .getTemplate(cp.getHome() + "/" + componentName + ".js")
          .process(model, writer);
    } catch (Exception e) {
      throw new RuntimeException("Failed to render component", e);
    }

    return writer.toString();
  }

  public String renderService(String serviceName) {
    StringWriter writer = new StringWriter();

    try {
      uiFreemarkerConfiguration
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

  public String renderStore(String storeName) {
    StringWriter writer = new StringWriter();
    Map<String, Object> model = scriptParameters.getAllParameters();

    try {
      String beanName = storeName.substring(0, storeName.lastIndexOf("."));
      //add store data to the script
      if(applicationContext.containsBean(beanName)) {
        Object store = applicationContext.getBean(beanName);
        try {
          var storeString = toJson(store);
          model.put("storeData", storeString);
        } catch (Exception e) {
          log.error("Failed to serialize " + storeName + "'s store data", e);
        }
      } else {
        log.info("Bean '{}' not found for the store {}", beanName, beanName);
      }

      uiFreemarkerConfiguration
          .getTemplate(storeName)
          .process(model, writer);

      return writer.toString();

    }  catch (InvalidReferenceException ire) {
      String missingVariable = ire.getBlamedExpressionString();
      log.error("Error: Missing data '{}' in the file '{}'", missingVariable, storeName);
    } catch (Exception e) {
      log.error("Error: Unable to process template: ", e);
    }
    return "";
  }
}