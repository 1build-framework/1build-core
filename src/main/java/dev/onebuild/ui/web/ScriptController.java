package dev.onebuild.ui.web;

import dev.onebuild.ui.domain.model.config.OneBuildConfigs;
import dev.onebuild.ui.domain.service.ScriptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import static dev.onebuild.ui.utils.ResourceUtils.readResource;

@Slf4j
@Controller
public class ScriptController {

  private final OneBuildConfigs oneBuildConfigs;
  private final ScriptService scriptService;

  public ScriptController(OneBuildConfigs oneBuildConfigs,
                          ScriptService scriptService) {
    this.oneBuildConfigs = oneBuildConfigs;
    this.scriptService = scriptService;
  }

  @GetMapping(value = "#{@oneBuildConfigs.getIndex().getPath()}", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public ResponseEntity<String> index() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_HTML);
    return ResponseEntity
        .status(HttpStatus.OK)
        .headers(headers)
        .body(scriptService.renderIndex());
  }

  @GetMapping(value = "#{@oneBuildConfigs.getCss().getPath()}/{cssName}", produces = "text/css")
  @ResponseBody
  public ResponseEntity<String> css(@PathVariable String cssName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(oneBuildConfigs.getCss().getSourcePath(), cssName));
  }

  @GetMapping(value = "#{@oneBuildConfigs.getJs().getPath()}/{jsName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> js(@PathVariable String jsName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(oneBuildConfigs.getJs().getSourcePath(), jsName));
  }

  @GetMapping(value = "#{@oneBuildConfigs.getComponents().getPath()}/{componentName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> component(@PathVariable String componentName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(scriptService.renderComponent(componentName));
  }

  @GetMapping(value = "#{@oneBuildConfigs.getService.getPath()}/{serviceName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> service(@PathVariable String serviceName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(scriptService.renderService(serviceName));
  }
}