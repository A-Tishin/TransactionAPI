package com.mintos.TransactionAPI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.TransactionAPI.dto.DataDto;
import com.mintos.TransactionAPI.exception.CurrencyPairExchangeNotSupportedException;
import com.mintos.TransactionAPI.utils.Currency;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            DataDto dto = new ObjectMapper().readValue(json, DataDto.class);
            return getRateForCurrency(dto, to);

        } catch (JsonProcessingException e) {
            throw new CurrencyPairExchangeNotSupportedException("Currency pair conversion is not supported.");
        }
    }

    private BigDecimal getRateForCurrency(DataDto dto, Currency currency) {
        Map<String, BigDecimal> map = dto.getData();
        if (map == null || map.get(currency.name()) == null) {
            throw new CurrencyPairExchangeNotSupportedException("Currency pair conversion is not supported.");
        }

        return map.get(currency.name()).setScale(10, RoundingMode.FLOOR);
    }

    private String getUriString(Currency from, Currency to) {
        return uriComponentsBuilder
                .queryParam("base_currency", from.name())
                .queryParam("currencies", to.name())
                .build()
                .toUriString();
    }
}
