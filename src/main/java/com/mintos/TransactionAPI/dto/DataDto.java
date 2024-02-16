package com.mintos.TransactionAPI.dto;

import java.math.BigDecimal;
import java.util.Map;

public class DataDto {

    Map<String, BigDecimal> data;

    public Map<String, BigDecimal> getData() {
        return data;
    }

    public void setData(Map<String, BigDecimal> exchange) {
        this.data = exchange;
    }
}
