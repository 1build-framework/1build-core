package dev.onebuild.ui.tests.config;

import dev.onebuild.ui.config.model.*;
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
  private CssConfigs cssConfigs;

  @Autowired
  private JsConfigs jsConfigs;

  @Autowired
  private ComponentsConfigs componentsConfigs;

  @Autowired
  private IndexConfigs indexConfigs;

  @Test
  public void testCssConfigProperties() {
    assertNotNull(cssConfigs);
    assertEquals("/libs/css", cssConfigs.getPath());
    assertEquals("/internal/ui/css", cssConfigs.getSourcePath());
    assertEquals(2, cssConfigs.getFiles().size());
    assertEquals("vuetify-3.5.2.min.css", cssConfigs.getFiles().get(ResourceName.VUETIFY.toString()));
    assertEquals("onebuild.css", cssConfigs.getFiles().get(ResourceName.DEFAULT.toString()));
  }

  @Test
  public void testJsConfigProperties() {
    assertNotNull(jsConfigs);
    assertEquals("/libs/js", jsConfigs.getPath());
    assertEquals("/internal/ui/js", jsConfigs.getSourcePath());
    assertEquals(4, jsConfigs.getJavascript().size());
    assertEquals("vue-3.4.15.global.prod.js", jsConfigs.getJavascript().get(ResourceName.VUE.toString()));
    assertEquals("vue-router-4.2.5.js", jsConfigs.getJavascript().get(ResourceName.VUE_ROUTER.toString()));
    assertEquals("vuetify-3.5.2.min.js", jsConfigs.getJavascript().get(ResourceName.VUETIFY.toString()));
    assertEquals("vue-3.4.19.esm-browser.js", jsConfigs.getModules().get(ResourceName.VUETIFY_ESM.toString()));
    assertEquals("onebuild.js", jsConfigs.getJavascript().get(ResourceName.DEFAULT.toString()));
  }

  @Test
  public void testIndexConfigProperties() {
    assertNotNull(indexConfigs);
    assertEquals("/index.html", indexConfigs.getPath());
    assertEquals("/internal/ui/templates", indexConfigs.getSourcePath());
    assertEquals("/internal/ui/templates", indexConfigs.getTemplateSourcePath());
    assertEquals("index.ftl", indexConfigs.getTemplate());
  }
}