package com.rhtech.newstack.repository;

import com.rhtech.newstack.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class CountryRowMapper implements RowMapper<Country> {

  @Override
  public Country mapRow(ResultSet resultSet, int i) throws SQLException {

    String code = resultSet.getString("Code");
    String name = resultSet.getString("Name");
    String continent = resultSet.getString("Continent");

    return new Country(code, name, continent);
  }
}
