package com.mintos.TransactionAPI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.TransactionAPI.exception.CurrencyPairExchangeNotSupportedException;
import com.mintos.TransactionAPI.utils.Currency;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyExchangeRateService {

    RestTemplate restTemplate;

    UriComponentsBuilder uriComponentsBuilder;

    public CurrencyExchangeRateService(RestTemplate restTemplate, UriComponentsBuilder uriComponentsBuilder) {
        this.restTemplate = restTemplate;
        this.uriComponentsBuilder = uriComponentsBuilder;
    }

    public BigDecimal getRateForCurrencyPair(Currency from, Currency to) {
        String uri = getUriString(from, to);

        try {
            String json = restTemplate.getForObject(uri, String.class);
            Map<String,Object> map = new ObjectMapper().readValue(json, HashMap.class);
            Double rate = getRateForCurrency(map, to);
            return new BigDecimal(rate).setScale(10, RoundingMode.FLOOR);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Double getRateForCurrency(Map<String,Object> map, Currency currency) {
        Map<String,Object> dataMap = (Map<String,Object>) map.get("data");
        if (dataMap == null || dataMap.get(currency.name()) == null) {
            throw new CurrencyPairExchangeNotSupportedException("Currency pair conversion is not supported.");
        }

        return (Double) dataMap.get(currency.name());
    }

    private String getUriString(Currency from, Currency to) {
        return uriComponentsBuilder
                .queryParam("base_currency", from.name())
                .queryParam("currencies", to.name())
                .build()
                .toUriString();
    }
}
