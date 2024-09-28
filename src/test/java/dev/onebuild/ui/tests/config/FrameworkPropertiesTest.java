package dev.onebuild.ui.tests.config;

import dev.onebuild.domain.model.OneBuildAppSettings;
import dev.onebuild.domain.model.ui.OneBuildIndex;
import dev.onebuild.domain.model.ui.OneBuildResources;
import dev.onebuild.domain.model.ui.ResourceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({ OneBuildAppSettings.class, UiTestConfiguration.class})
@ActiveProfiles("test")
public class FrameworkPropertiesTest {
  @Autowired
  private List<OneBuildResources> resources;

  @Autowired
  private OneBuildIndex oneBuildIndex;

  @Test
  public void testCssConfigProperties() {
    List<OneBuildResources> cssLocations = resources.stream()
        .filter(resource -> resource.getResourceType() == ResourceType.CSS)
        .collect(Collectors.toList());
    assertNotNull(cssLocations);
    assertFalse(cssLocations.isEmpty());
    OneBuildResources cssConfig = cssLocations.get(0);
    assertNotNull(cssConfig);
    assertEquals("/libs/css", cssConfig.getPath());
    assertEquals("/internal/ui/css", cssConfig.getSourcePath());
    assertEquals(2, cssConfig.getResources().size());
    assertEquals("vuetify-css-3.7.0.css", cssConfig.getResources().get(0));
    assertEquals("onebuild.css", cssConfig.getResources().get(1));
  }

  @Test
  public void testJsConfigProperties() {
    List<OneBuildResources> jsConfigs = resources.stream()
        .filter(resource -> resource.getResourceType() == ResourceType.JS)
        .collect(Collectors.toList());
    assertNotNull(jsConfigs);
    assertFalse(jsConfigs.isEmpty());
    OneBuildResources jsConfig = jsConfigs.get(0);
    assertEquals("/libs/js", jsConfig.getPath());
    assertEquals("/internal/ui/js", jsConfig.getSourcePath());
    assertEquals(6, jsConfig.getResources().size());
    assertEquals("vue-3.4.38.js", jsConfig.getResources().get(0));
    assertEquals("vuedemi-2.2.2.js", jsConfig.getResources().get(1));
    assertEquals("vuetify-3.7.0.js", jsConfig.getResources().get(2));
    assertEquals("vue-router-4.4.3.js", jsConfig.getResources().get(3));
    assertEquals("pinia-2.2.2.js", jsConfig.getResources().get(4));
  }

  @Test
  public void testIndexConfigProperties() {
    assertNotNull(oneBuildIndex);
    assertEquals("/index.html", oneBuildIndex.getPath());
    assertEquals("/internal/ui/index", oneBuildIndex.getSourcePath());
    assertEquals("index.html.ftl", oneBuildIndex.getTemplate());
  }
}