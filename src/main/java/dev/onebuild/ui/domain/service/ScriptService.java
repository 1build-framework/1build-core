package dev.onebuild.ui.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.ui.domain.model.config.OneBuildUiConfigs;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.domain.model.config.UiComponent;
import freemarker.core.InvalidReferenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.io.StringWriter;
import java.util.Map;

import static dev.onebuild.ui.utils.AppUtils.toJson;
import static dev.onebuild.ui.utils.ResourceUtils.readResource;

@Slf4j
public class ScriptService {
  private final ObjectMapper mapper;
  private final OneBuildUiConfigs oneBuildUiConfigs;
  private final freemarker.template.Configuration uiFreemarkerConfiguration;
  private final ScriptParameters scriptParameters;
  private final ApplicationContext applicationContext;

  public ScriptService(ObjectMapper mapper,
                       OneBuildUiConfigs oneBuildUiConfigs,
                       freemarker.template.Configuration uiFreemarkerConfiguration,
                       ScriptParameters scriptParameters,
                       ApplicationContext applicationContext) {
    this.mapper = mapper;
    this.oneBuildUiConfigs = oneBuildUiConfigs;
    this.uiFreemarkerConfiguration = uiFreemarkerConfiguration;
    this.scriptParameters = scriptParameters;
    this.applicationContext = applicationContext;
  }

  public String renderIndex() {
    StringWriter writer = new StringWriter();

    String mainComponentName = oneBuildUiConfigs.getIndex().getMainComponent();

    UiComponent cp = oneBuildUiConfigs.getComponent().getList().get(mainComponentName);

    if(cp != null) {
      String importPath = oneBuildUiConfigs.getComponent().getPath() + "/" + mainComponentName;

      Map<String, Object> model = scriptParameters.getAllParameters();
      model.putAll(Map.of(
          "cssConfig", oneBuildUiConfigs.getCss(),
          "jsConfig", oneBuildUiConfigs.getJs(),
          "componentImportPath", importPath
      ));

      try {
        uiFreemarkerConfiguration
            .getTemplate(oneBuildUiConfigs.getIndex().getTemplate())
            .process(model, writer);
      } catch (Exception e) {
        throw new RuntimeException("Failed to render index", e);
      }
    }

    return writer.toString();
  }

  public String renderComponent(String componentName) {
    StringWriter writer = new StringWriter();

    UiComponent cp = oneBuildUiConfigs.getComponent().getList().get(componentName);
    if(cp == null) {
      return "";
    }

    String resourcePath = oneBuildUiConfigs.getComponent().getSourcePath() + "/" +
        cp.getHome();

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