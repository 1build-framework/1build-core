package dev.onebuild.ui.tests.web.components;

import dev.onebuild.domain.model.OneBuildAppSettings;
import dev.onebuild.qa.html.api.BrowserApp;
import dev.onebuild.qa.html.vuetify.BrowserAppFactory;
import dev.onebuild.ui.tests.config.UiTestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("search")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({ OneBuildAppSettings.class,
    UiTestConfiguration.class
})
public class SearchComponentTest {
  @LocalServerPort
  private int port;

  private BrowserApp app;

  @BeforeEach
  public void beforeEach() {
    app = BrowserAppFactory.createBrowserAppInContainer(
        port,
        "/index.html",
        "/Users/msabir/development/projects/1build-projects/1build-ui/src/test/resources/debug");
  }

  @AfterEach
  public void afterEach() {
    app.destroy();
  }

  @Test
  public void whenSearchAppStarts_itLoadsTheHomePage() {

    assertTrue(app.exists("searchText"));
    assertTrue(app.exists("searchButton"));

    app.setInputValue("searchText","Hello, ");
    app.click("searchButton");

    assertEquals("Hello, World", app.getInputValue("searchText"));
  }
}