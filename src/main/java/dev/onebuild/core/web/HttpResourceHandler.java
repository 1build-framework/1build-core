package dev.onebuild.core.web;

import dev.onebuild.commons.domain.model.ui.OneBuildResources;
import dev.onebuild.commons.domain.model.ui.ResourceType;
import dev.onebuild.core.domain.service.ScriptService;
import dev.onebuild.commons.errors.OneBuildExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static dev.onebuild.core.utils.ResourceUtils.readResource;

/*
  * This class handles HTTP requests for resources.
  * It reads the requested resource and returns it as a string.
  * The resources are read from the source path.
  * This handler is called from the endpoint which is created dynamically in the class OneBuildEndpointRegistrar.
 */
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
        .filter(css -> path.startsWith(css.getWebPath()))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createMissingConfigException(path));

    return readResource(prodEnabled, location.getSourcePath(), resourceName);
  }

  @ResponseBody
  public String renderJsResource(HttpServletRequest request, @PathVariable(name = "jsName") String resourceName) {
    String path = request.getRequestURI();

    OneBuildResources location = resources.stream()
        .filter(resource -> resource.getResourceType() == ResourceType.JS)
        .filter(js -> path.startsWith(js.getWebPath()))
        .findFirst()
        .orElseThrow(() -> exceptionFactory.createMissingConfigException(path));

    return readResource(prodEnabled, location.getSourcePath(), resourceName);
  }

  @ResponseBody
  public String renderScript(HttpServletRequest request, @PathVariable(name = "scriptName") String scriptName) {
    String requestUri = request.getRequestURI();
    String contextPath = requestUri.substring(0, requestUri.lastIndexOf(scriptName) - 1);

    return scriptService.renderScript(contextPath, scriptName);
  }

  @ResponseBody
  public String renderValidator() {
    return scriptService.renderValidator();
  }

  @ResponseBody
  public String renderIndex() {
    return scriptService.renderIndex();
  }

}