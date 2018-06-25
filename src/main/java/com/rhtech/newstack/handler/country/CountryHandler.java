package com.rhtech.newstack.handler.country;

import com.rhtech.newstack.model.entity.Country;
import com.rhtech.newstack.repository.CountryRepository;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryHandler implements HttpHandler {

  private final CountryRepository countryRepository;

  @Autowired
  public CountryHandler(RoutingHandler handler, CountryRepository countryRepository) {
    handler.get("/country", this::handleRequest);
    this.countryRepository = countryRepository;
  }

  @Override
  public void handleRequest(HttpServerExchange httpServerExchange) {

    Optional<Country> result = countryRepository.findByCountryCode("KK");
    if (result.isPresent()) {
      httpServerExchange.getResponseSender().send(result.get().getName());
    } else {
      httpServerExchange.getResponseSender().send("No Country Found!");
    }
  }
}
