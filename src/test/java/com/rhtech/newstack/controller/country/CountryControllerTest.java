package com.rhtech.newstack.controller.country;

import com.rhtech.newstack.HttpServerExchangeStub;
import com.rhtech.newstack.repository.CountryRepository;
import com.rhtech.newstack.service.CountryService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.HttpString;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CountryControllerTest {

  private HikariDataSource dataSource;
  private JdbcTemplate jdbcTemplate;
  private RoutingHandler routingHandler;

  private Undertow server;

  @BeforeAll
  public void initialize() {

    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/world");
    hikariConfig.setUsername("user");
    hikariConfig.setPassword("password");
    hikariConfig.setMaximumPoolSize(1);
    // create datasource and jdbctemplate
    dataSource = new HikariDataSource(hikariConfig);
    jdbcTemplate = new JdbcTemplate(dataSource);
    // create routing handler
    routingHandler = new RoutingHandler();
  }

  @AfterAll
  public void cleanUp() {
    dataSource.close();
  }

  @Test
  public void test() throws Exception {

    // set the datasource here
    CountryRepository countryRepository = new CountryRepository(jdbcTemplate);
    CountryService countryService = new CountryService(countryRepository);
    CountryController countryController = new CountryController(routingHandler, countryService);



    assertEquals("hello", "hello");
  }

}
