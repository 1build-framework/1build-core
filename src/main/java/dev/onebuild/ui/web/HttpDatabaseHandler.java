package dev.onebuild.ui.web;

import dev.onebuild.domain.model.db.DefaultOneBuildRecord;
import dev.onebuild.domain.model.db.OneBuildRecord;
import dev.onebuild.domain.service.OneBuildDataService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

public class HttpDatabaseHandler {
  private final OneBuildDataService oneBuildDataService;
  public HttpDatabaseHandler(OneBuildDataService oneBuildDataService) {
    this.oneBuildDataService = oneBuildDataService;
  }

  @ResponseBody
  public ResponseEntity<OneBuildRecord> findById(HttpServletRequest request, @RequestParam("id") Long id) {
    if(oneBuildDataService == null) {
      return null;
    }
    var path = request.getRequestURI();
    return ResponseEntity.ok(oneBuildDataService.findById(path, id));
  }

  @ResponseBody
  public ResponseEntity<List<OneBuildRecord>> findAll(HttpServletRequest request) {
    if(oneBuildDataService == null) {
      return null;
    }
    var path = request.getRequestURI();
    return ResponseEntity.ok(oneBuildDataService.findAll(path));
  }

  @ResponseBody
  public ResponseEntity<OneBuildRecord> save(HttpServletRequest request, @RequestBody Map<String, Object> body) {
    if(oneBuildDataService == null) {
      return null;
    }
    var path = request.getRequestURI();
    OneBuildRecord record = DefaultOneBuildRecord.from(body);
    oneBuildDataService.save(path, record);
    return ResponseEntity.ok(record);
  }

  @ResponseBody
  public void updateById(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
    if(oneBuildDataService == null) {
      return;
    }
    var path = request.getRequestURI();
    OneBuildRecord record = DefaultOneBuildRecord.from(body);
    record.setId(id);
    oneBuildDataService.save(path, record);
  }

  @ResponseBody
  public void deleteById(HttpServletRequest request, @PathVariable("id") Long id) {
    if(oneBuildDataService == null) {
      return;
    }
    var path = request.getRequestURI();
    oneBuildDataService.deleteById(path, id);
  }
}