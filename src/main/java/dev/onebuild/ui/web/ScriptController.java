package dev.onebuild.ui.web;

import dev.onebuild.ui.config.model.*;
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
  private final CssConfigs cssConfigs;
  private final JsConfigs jsConfigs;
  private final ServiceConfigs serviceConfigs;
//  private final FontConfigs fontConfigs;
  private final ScriptService scriptService;

  public ScriptController(CssConfigs cssConfigs,
                          JsConfigs jsConfigs,
//                          FontConfigs fontConfigs,
                          ServiceConfigs serviceConfigs,
                          ScriptService scriptService) {
    this.cssConfigs = cssConfigs;
    this.jsConfigs = jsConfigs;
//    this.fontConfigs = fontConfigs;
    this.serviceConfigs = serviceConfigs;
    this.scriptService = scriptService;
  }

  @GetMapping(value = "#{@indexConfigs.getPath()}", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public ResponseEntity<String> index() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_HTML);
    return ResponseEntity
        .status(HttpStatus.OK)
        .headers(headers)
        .body(scriptService.renderIndex());
  }

  @GetMapping(value = "#{@cssConfigs.getPath()}/{cssName}", produces = "text/css")
  @ResponseBody
  public ResponseEntity<String> css(@PathVariable String cssName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(cssConfigs.getSourcePath(), cssName));
  }

  @GetMapping(value = "#{@jsConfigs.getPath()}/{jsName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> js(@PathVariable String jsName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(jsConfigs.getSourcePath(), jsName));
  }

  @GetMapping(value = "#{@componentsConfigs.getPath()}/{componentName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> component(@PathVariable String componentName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(scriptService.renderComponent(componentName));
  }

  /*@GetMapping(value = "#{@fontConfigs.getPath()}/{fontName}")
  @ResponseBody
  public ResponseEntity<String> fonts(@PathVariable String fontName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(fontConfigs.getSourcePath(), fontName));
  }*/

  @GetMapping(value = "#{@serviceConfigs.getPath()}/{serviceName}", produces = "text/javascript")
  @ResponseBody
  public ResponseEntity<String> service(@PathVariable String serviceName) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readResource(serviceConfigs.getSourcePath(), serviceName));
  }
}