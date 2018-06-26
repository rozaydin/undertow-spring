package com.rhtech.newstack.controller.country;

import com.rhtech.newstack.model.entity.Country;
import com.rhtech.newstack.service.CountryService;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryController {

  @Autowired
  public CountryController(RoutingHandler routingHandler, CountryService countryService) {

    // Register handlers
    routingHandler.get("country/{code}", createGetCountryByCode2HttpHandler(countryService));
  }

  // Handlers

  private static HttpHandler createGetCountryByCode2HttpHandler(CountryService countryService) {

    return exchange -> {
      if (exchange.isInIoThread()) {
        exchange.dispatch(
            exchange.getDispatchExecutor(),
            () -> {
              try {
                // extract parameters
                String code = exchange.getQueryParameters().get("code").getFirst();
                // extract body
                exchange.getRequestReceiver().receiveFullBytes((exc, data) -> {});

                // invoke service
                Optional<Country> country = countryService.getCountry(code);

                if (country.isPresent()) {
                  exchange.setStatusCode(StatusCodes.OK);
                  exchange.getResponseSender().send(country.get().getName());
                } else {
                  exchange.setStatusCode(StatusCodes.NOT_FOUND);
                }

              } finally {
                exchange.endExchange();
              }
            });
      }
    };
  }
}
