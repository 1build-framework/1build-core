package dev.onebuild.ui.tests.config;

import dev.onebuild.ui.domain.model.config.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FrameworkPropertiesTest {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FrameworkPropertiesTest.class);

  @Autowired
  private OneBuildUiConfigs oneBuildUiConfigs;

  @Test
  public void testCssConfigProperties() {
    CssConfig cssConfig = oneBuildUiConfigs.getCss();
    assertNotNull(cssConfig);
    assertEquals("/libs/css", cssConfig.getPath());
    assertEquals("/internal/ui/css", cssConfig.getSourcePath());
    assertEquals(2, cssConfig.getFiles().size());
    assertEquals("vuetify-css-3.7.0.css", cssConfig.getFiles().get(ResourceName.VUETIFY.toString()));
    assertEquals("onebuild.css", cssConfig.getFiles().get(ResourceName.DEFAULT.toString()));
  }

  @Test
  public void testJsConfigProperties() {
    JsConfig jsConfig = oneBuildUiConfigs.getJs();
    assertNotNull(jsConfig);
    assertEquals("/libs/js", jsConfig.getPath());
    assertEquals("/internal/ui/js", jsConfig.getSourcePath());
    assertEquals(5, jsConfig.getJavascript().size());
    assertEquals("vue-3.4.38.js", jsConfig.getJavascript().get(ResourceName.VUE.toString()));
    assertEquals("vue-router-4.4.3.js", jsConfig.getJavascript().get(ResourceName.VUE_ROUTER.toString()));
    assertEquals("vuetify-3.7.0.js", jsConfig.getJavascript().get(ResourceName.VUETIFY.toString()));
    assertEquals("pinia-2.2.2.js", jsConfig.getJavascript().get(ResourceName.PINIA.toString()));
  }

  @Test
  public void testIndexConfigProperties() {
    IndexConfig indexConfig = oneBuildUiConfigs.getIndex();
    assertNotNull(indexConfig);
    assertEquals("/index.html", indexConfig.getPath());
    assertEquals("/internal/ui/templates", indexConfig.getSourcePath());
    assertEquals("/internal/ui/templates", indexConfig.getTemplateSourcePath());
    assertEquals("index.ftl", indexConfig.getTemplate());
  }
}