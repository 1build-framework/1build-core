package dev.onebuild.ui.tests.config;

import dev.onebuild.ui.domain.model.config.ComponentsConfigs;
import dev.onebuild.ui.domain.model.config.IndexConfigs;
import dev.onebuild.ui.domain.model.config.OneBuildConfigs;
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
  private OneBuildConfigs oneBuildConfigs;

  @Test
  public void testComponentsConfigProperties() {
    ComponentsConfigs componentsConfigs = oneBuildConfigs.getComponents();
    IndexConfigs indexConfigs = oneBuildConfigs.getIndex();

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