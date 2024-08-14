package dev.onebuild.ui.tests.config;

import dev.onebuild.ui.config.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ComponentPropertiesTest {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ComponentPropertiesTest.class);
  @Autowired
  private ComponentsConfigs componentsConfigs;

  @Autowired
  private IndexConfigs indexConfigs;

  @Test
  public void testComponentsConfigProperties() {
    assertNotNull(componentsConfigs);
    assertEquals("About", indexConfigs.getMainComponent());
    assertEquals("/app/components", componentsConfigs.getPath());
    assertEquals("/app/modules", componentsConfigs.getSourcePath());
    assertEquals(2, componentsConfigs.getList().size());

    var app = componentsConfigs.getList().get("App");
    assertEquals("/app", app.getHome());
    assertEquals("app", app.getName());

    var about = componentsConfigs.getList().get("About");
    assertEquals("/about", about.getHome());
    assertEquals("about", about.getName());
  }
}