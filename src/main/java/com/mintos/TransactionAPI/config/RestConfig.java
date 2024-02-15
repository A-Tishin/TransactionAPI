package com.mintos.TransactionAPI.config;

import com.mintos.TransactionAPI.handler.RestTemplateErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class RestConfig {

    @Value("${api.host}")
    private String host;

    @Value("${api.key}")
    private String key;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }

    @Bean
    public UriComponentsBuilder uriComponentsBuilder() {
        return UriComponentsBuilder
                .fromHttpUrl(host)
                .queryParam("apikey", key);
    }

}
