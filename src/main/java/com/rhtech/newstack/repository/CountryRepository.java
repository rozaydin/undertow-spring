package com.rhtech.newstack.repository;

import com.rhtech.newstack.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class CountryRepository {

  private final JdbcTemplate jdbcTemplate;
  private final CountryRowMapper rowMapper;

  // SQL Queries
  private static final String findByCountryCode2_SQL = "SELECT * FROM Country WHERE code2 = ?";

  @Autowired
  public CountryRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.rowMapper = new CountryRowMapper();
  }

  public Optional<Country> findByCountryCode(String code2) {

    try {
      return Optional.of(jdbcTemplate.queryForObject(findByCountryCode2_SQL, rowMapper, code2));
    } catch (EmptyResultDataAccessException ee) {
      return Optional.empty();
    }
  }
}
