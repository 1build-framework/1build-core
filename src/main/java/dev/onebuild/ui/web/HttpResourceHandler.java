package dev.onebuild.ui.web;

import dev.onebuild.domain.model.ui.OneBuildResources;
import dev.onebuild.domain.model.ui.ResourceType;
import dev.onebuild.ui.domain.service.ScriptService;
import dev.onebuild.errors.OneBuildExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static dev.onebuild.ui.utils.ResourceUtils.readResource;

public class HttpResourceHandler {

  private final boolean prodEnabled;
  private final List<OneBuildResources> resources;
  private final OneBuildExceptionFactory exceptionFactory;
  private final ScriptService scriptService;

  public HttpResourceHandler(
      boolean prodEnabled,
      List<OneBuildResources> resources,
      ScriptService scriptService,
      OneBuildExceptionFactory exceptionFactory) {
    this.prodEnabled = prodEnabled;
    this.resources = resources;
    this.scriptService = scriptService;
    this.exceptionFactory = exceptionFactory;
  }

  @ResponseBody
  public String renderCssResource(HttpServletRequest request, @PathVariable(name = "cssName") String resourceName) {
    String path = request.getRequestURI();

    OneBuildResources location = resources.stream()
        .filter(resource -> resource.getResourceType() == ResourceType.CSS)
        .filter(css -> path.startsWith(css.getPath()))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createMissingConfigException(path));

    return readResource(prodEnabled, location.getSourcePath(), resourceName);
  }

  @ResponseBody
  public String renderJsResource(HttpServletRequest request, @PathVariable(name = "jsName") String resourceName) {
    String path = request.getRequestURI();

    OneBuildResources location = resources.stream()
        .filter(resource -> resource.getResourceType() == ResourceType.JS)
        .filter(js -> path.startsWith(js.getPath()))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createMissingConfigException(path));

    return readResource(prodEnabled, location.getSourcePath(), resourceName);
  }

  @ResponseBody
  public String renderComponent(@PathVariable(name = "componentName") String componentName) {
    return scriptService.renderComponent(componentName);
  }

  @ResponseBody
  public String renderService(@PathVariable(name = "serviceName") String serviceName) {
    return scriptService.renderService(serviceName);
  }

  @ResponseBody
  public String renderStore(@PathVariable(name = "storeName") String storeName) {
    return scriptService.renderStore(storeName);
  }

  @ResponseBody
  public String renderIndex() {
    return scriptService.renderIndex();
  }

}