package dev.onebuild.ui.tests.web.components;

import dev.onebuild.testing.html.api.IdValue;
import dev.onebuild.testing.html.api.MultiValueData;
import dev.onebuild.ui.tests.web.AbstractBrowserTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.runners.model.MultipleFailureException.assertEmpty;

@ActiveProfiles("datasource")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataSourceTest extends AbstractBrowserTest {

  @Test
  public void whenDatasourceAppStarts_itLoadsTheHomePage() {
    assertTrue(app.exists("datasourceContainer"));
    assertTrue(app.exists("databaseTypes"));

    MultiValueData datasourceTypes = app.getMultiValueData("databaseTypes");

    app.printLogs();

    assertEquals(4, datasourceTypes.getValues().size());
    assertEquals(0, datasourceTypes.getSelectedValues().size());

    assertTrue(app.exists("jdbcUrl"));
    assertEquals("", app.getValue("jdbcUrl"));

    assertTrue(app.exists("username"));
    assertEquals("", app.getValue("username"));

    assertTrue(app.exists("password"));
    assertEquals("", app.getValue("password"));
  }

  @Test
  @Disabled
  public void whenDatasourceInfoIsEnteredForPG_itSavesTheData() {
  }

  protected String getDebugFolder() {
    return "/Users/msabir/development/projects/1build-projects/1build-ui/src/test/resources/debug";
  }
}
