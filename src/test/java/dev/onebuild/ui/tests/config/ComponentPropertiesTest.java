package dev.onebuild.ui.tests.config;

import dev.onebuild.commons.domain.model.OneBuildAppSettings;
import dev.onebuild.commons.domain.model.ui.OneBuildScripts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({
    OneBuildAppSettings.class,
    UiTestConfiguration.class
})
@ActiveProfiles("test")
public class ComponentPropertiesTest {

  @Autowired
  private List<OneBuildScripts> components;

  @Autowired
  private OneBuildAppSettings appSettings;


  @Test
  public void testComponentsConfigProperties() {

    assertNotNull(components);
    assertEquals(4, components.size());

    assertEquals("about", appSettings.getMainComponent());

    OneBuildScripts uiComponents = this.components.get(0);
    assertEquals("/app/components", uiComponents.getWebPath());
    assertEquals("/app/modules", uiComponents.getSourcePath());
    assertEquals(2, uiComponents.getList().size());

    var app = uiComponents.getList().stream()
        .filter(c -> c.getName().equalsIgnoreCase("app"))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Main component not found"));

    //var app = uiComponents.getList().get("app");
    assertEquals("/app", app.getHome());

    var about = uiComponents.getList().stream()
        .filter(c -> c.getName().equalsIgnoreCase("about"))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Main component not found"));

    assertEquals("/about", about.getHome());
  }
}