package dev.onebuild.ui.tests.web.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onebuild.testing.html.api.IdValue;
import dev.onebuild.testing.html.api.MultiValueData;
import dev.onebuild.ui.domain.model.config.ScriptParameters;
import dev.onebuild.ui.tests.web.AbstractComponentTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("datasource")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class DataSourceComponentTest extends AbstractComponentTest {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataSourceComponentTest.class);

  @LocalServerPort
  private int port;

  @Autowired
  @Qualifier("scriptParameters")
  private ScriptParameters scriptParameters;
  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  public void beforeEach() {
    try {
      List<IdValue> databaseParameters = List.of(
          IdValue.builder().id("postgreSqlId").value("PostgreSQL").build(),
          IdValue.builder().id("mySqlId").value("My SQL").build(),
          IdValue.builder().id("oracleId").value("Oracle").build());
      scriptParameters.addParameter("databaseTypes", mapper.writeValueAsString(databaseParameters));
    } catch (Exception e) {
      fail("scriptParameters should be injected", e);
    }
    super.openBrowser();
  }

  @AfterEach
  public void afterEach() {
    super.closeBrowser();
  }

  @Test
  public void whenDatasourceAppStarts_itLoadsTheHomePage() {
    assertTrue(app.exists("datasourceContainer"));
    assertTrue(app.exists("databaseTypes"));

    app.setSelectValue("databaseTypes", "Oracle");

    MultiValueData datasourceTypes = app.getMultiValueData("databaseTypes");

    app.printLogs();

    assertEquals(3, datasourceTypes.getValues().size());
    assertEquals(1, datasourceTypes.getSelectedValues().size());

    List<String> expectedValues = List.of("PostgreSQL", "My SQL", "Oracle");
    List<String> values = datasourceTypes.getValues().stream().map(IdValue::getValue).toList();
    assertEquals(expectedValues, values);

    IdValue selectedValue = datasourceTypes.getSelectedValues().get(0);

    assertEquals("oracleId", selectedValue.getId());

    assertTrue(app.exists("jdbcUrl"));
    assertEquals("", app.getInputValue("jdbcUrl"));

    assertTrue(app.exists("username"));
    assertEquals("", app.getInputValue("username"));

    assertTrue(app.exists("password"));
    assertEquals("", app.getInputValue("password"));
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
