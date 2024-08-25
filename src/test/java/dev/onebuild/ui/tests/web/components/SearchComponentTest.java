package dev.onebuild.ui.tests.web.components;

import dev.onebuild.ui.tests.web.AbstractComponentTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("search")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchComponentTest extends AbstractComponentTest {
  @LocalServerPort
  private int port;

  @BeforeEach
  public void beforeEach() {
    super.openBrowser();
  }

  @AfterEach
  public void afterEach() {
    super.closeBrowser();
  }

  @Test
  public void whenSearchAppStarts_itLoadsTheHomePage() {

    assertTrue(app.exists("searchText"));
    assertTrue(app.exists("searchButton"));

    app.setInputValue("searchText","Hello, ");
    app.click("searchButton");

    assertEquals("Hello, World", app.getInputValue("searchText"));
  }

  protected String getDebugFolder() {
    return "/Users/msabir/development/projects/1build-projects/1build-ui/src/test/resources/debug";
  }

  protected String getHomePageUrl() {
    return "/index.html";
  }

  public int getPort() {
    return port;
  }
}