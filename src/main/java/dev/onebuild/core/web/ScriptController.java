package dev.onebuild.core.web;

import dev.onebuild.commons.domain.model.OneBuildAppSettings;
import dev.onebuild.commons.domain.model.ui.OneBuildIndex;
import dev.onebuild.core.domain.service.ScriptService;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class ScriptController {
  private final OneBuildAppSettings oneBuildAppSettings;
  private final OneBuildIndex oneBuildIndex;
  private final ScriptService scriptService;

  public ScriptController(OneBuildAppSettings oneBuildAppSettings,
                          OneBuildIndex oneBuildIndex,
                          ScriptService scriptService) {
    this.oneBuildAppSettings = oneBuildAppSettings;
    this.oneBuildIndex = oneBuildIndex;
    this.scriptService = scriptService;

  }

  /*@GetMapping(value = "#{@oneBuildIndex.getPath()}", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public ResponseEntity<String> index() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_HTML);
    return ResponseEntity
        .status(HttpStatus.OK)
        .headers(headers)
        .body(scriptService.renderIndex());
  }*/
}




/*@GetMapping(value = "#{@oneBuildUiConfigs.getCss().getPath()}/{cssName}", produces = "text/css")
  @ResponseBody
  public ResponseEntity<String> css(@PathVariable String cssName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(oneBuildUiConfigs.isProdEnabled(), oneBuildUiConfigs.getCss().get(), cssName));
  }

  @GetMapping(value = "#{@oneBuildUiConfigs.getJs().getPath()}/{jsName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> js(@PathVariable String jsName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(oneBuildUiConfigs.isProdEnabled(), oneBuildUiConfigs.getJs().getSourcePath(), jsName));
  }

  @GetMapping(value = "#{@oneBuildUiConfigs.getComponent().getPath()}/{componentName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> component(@PathVariable String componentName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(scriptService.renderComponent(componentName));
  }

  @GetMapping(value = "#{@oneBuildUiConfigs.getService.getPath()}/{serviceName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> service(@PathVariable String serviceName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(scriptService.renderService(serviceName));
  }

  @GetMapping(value = "#{@oneBuildUiConfigs.getStore().getPath()}/{storeName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> store(@PathVariable String storeName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(scriptService.renderStore(storeName));
  }*/