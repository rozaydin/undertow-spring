package com.rhtech.newstack.service;

import com.rhtech.newstack.model.entity.Country;
import com.rhtech.newstack.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Optional<Country> getCountry(String countryCode2) {
        return countryRepository.findByCountryCode(countryCode2);
    }

}
