package dev.onebuild.core.web;

import dev.onebuild.commons.domain.model.db.DefaultOneBuildRecord;
import dev.onebuild.commons.domain.model.db.OneBuildRecord;
import dev.onebuild.commons.domain.service.OneBuildDataService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class HttpDatabaseHandler {
  private final OneBuildDataService oneBuildDataService;
  public HttpDatabaseHandler(OneBuildDataService oneBuildDataService) {
    this.oneBuildDataService = oneBuildDataService;
  }

  @ResponseBody
  public ResponseEntity<List<OneBuildRecord>> find(HttpServletRequest request,
                                                   @RequestParam MultiValueMap<String, String> params) {
    if(oneBuildDataService == null) {
      return null;
    }
    var path = request.getRequestURI();
    Map<String, Object> parameters = new HashMap<>();
    params.forEach((key, value) -> parameters.put(key, params.get(key)));
    return ResponseEntity.ok(oneBuildDataService.find(path, parameters));
  }

  @ResponseBody
  public ResponseEntity<OneBuildRecord> create(HttpServletRequest request,
                                               @RequestBody Map<String, Object> body) {
    if(oneBuildDataService == null) {
      return null;
    }
    var path = request.getRequestURI();
    OneBuildRecord record = DefaultOneBuildRecord.from(body);
    oneBuildDataService.save(path, path, record);
    return ResponseEntity.ok(record);
  }

  @ResponseBody
  public ResponseEntity<OneBuildRecord> update(HttpServletRequest request,
                                               @PathVariable("id") Long id,
                                               @RequestBody Map<String, Object> body) {
    if(oneBuildDataService == null) {
      return null;
    }
    String requestUri = request.getRequestURI();
    String contextPath = requestUri.substring(0, requestUri.lastIndexOf(id.toString()) - 1);
    OneBuildRecord record = DefaultOneBuildRecord.from(body);
    record.setId(id);
    oneBuildDataService.save(contextPath, requestUri, record);
    return ResponseEntity.ok(record);
  }

  @ResponseBody
  public ResponseEntity<?> delete(HttpServletRequest request,
                                  @RequestHeader("X-DELETE-IDS") String idsString) {
    if(oneBuildDataService == null) {
      return null;
    }
    String requestUri = request.getRequestURI();
    if(StringUtils.isBlank(idsString)) {
      return ResponseEntity.badRequest().build();
    }
    List<Long> ids = Stream.of(idsString.split(",")).map(String::trim).map(Long::parseLong).toList();
    oneBuildDataService.delete(requestUri, ids);
    return ResponseEntity.ok().build();
  }
}