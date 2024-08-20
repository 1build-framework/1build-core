package dev.onebuild.ui.tests.web.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.testing.html.api.IdValue;
import dev.onebuild.testing.html.api.MultiValueData;
import dev.onebuild.ui.tests.web.AbstractBrowserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("datasource")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class DataSourceTest extends AbstractBrowserTest {

  private final ObjectMapper mapper = new ObjectMapper();

  protected void beforeEach() {
    clearData();
  }

  @Test
  public void whenDatasourceAppStarts_itLoadsTheHomePage() {
    List<IdValue> databaseParameters = List.of(
        IdValue.builder().id("postgreSqlId").value("PostgreSQL").build(),
        IdValue.builder().id("mySqlId").build(),
        IdValue.builder().id("oracleId").value("Oracle").build());

    try {
      setData("databaseTypes", mapper.writeValueAsString(databaseParameters));
    } catch (Exception e) {
      e.printStackTrace();
    }

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
