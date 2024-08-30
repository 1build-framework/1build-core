package dev.onebuild.ui.tests.config;

import dev.onebuild.ui.domain.model.config.ComponentsConfig;
import dev.onebuild.ui.domain.model.config.IndexConfig;
import dev.onebuild.ui.domain.model.config.OneBuildUiConfigs;
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
  private OneBuildUiConfigs oneBuildUiConfigs;

  @Test
  public void testComponentsConfigProperties() {
    ComponentsConfig componentsConfig = oneBuildUiConfigs.getComponent();
    IndexConfig indexConfig = oneBuildUiConfigs.getIndex();

    assertNotNull(componentsConfig);
    assertEquals("about", indexConfig.getMainComponent());
    assertEquals("/app/components", componentsConfig.getPath());
    assertEquals("/app/modules", componentsConfig.getSourcePath());
    assertEquals(2, componentsConfig.getList().size());

    var app = componentsConfig.getList().get("app");
    assertEquals("/app", app.getHome());

    var about = componentsConfig.getList().get("about");
    assertEquals("/about", about.getHome());
  }
}