package com.mintos.TransactionAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.TransactionAPI.dto.AccountDto;
import com.mintos.TransactionAPI.utils.Currency;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class AccountControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RestTemplate restTemplate;

    @Test
    @Order(1)
    public void testAccountCreation() throws Exception {
        AccountDto dto = new AccountDto();
        dto.setCurrency(Currency.USD);
        dto.setBalance(BigDecimal.TEN);

        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountCurrency").value(Currency.USD.name()));
    }

    @Test
    @Order(2)
    public void testAccountAssociatedWithUserCreated() throws Exception {
        AccountDto dto = new AccountDto();
        dto.setCurrency(Currency.EUR);
        dto.setBalance(BigDecimal.TEN);
        dto.setUserId(1L);

        mvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.accountCurrency").value(Currency.EUR.name()));

        mvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userAccounts.length()").value(2));

    }

    @Test
    @Order(3)
    public void testFundTransferExternalServiceError() throws Exception {
        Mockito.when(restTemplate.getForObject(any(String.class), any()))
                .thenReturn("{\"error\":{\"bad\": \"json\"}}");

        mvc.perform(MockMvcRequestBuilders.put("/accounts/transfer")
                        .param("senderId", "1")
                        .param("receiverId", "2")
                        .param("amount", "5"))
                .andExpect(status().is(503));
    }

    @Test
    @Order(4)
    public void testFundTransferPairNotSupported() throws Exception {
        Mockito.when(restTemplate.getForObject(any(String.class), any()))
                .thenReturn("{\"error\":{\"reason\": \"currency is not supported\"}}");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/accounts/transfer")
                        .param("senderId", "1")
                        .param("receiverId", "2")
                        .param("amount", "5"))
                .andExpect(status().is(503))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "Currency pair conversion is not supported.");
    }

    @Test
    @Order(5)
    public void testFundTransfer() throws Exception {
        Mockito.when(restTemplate.getForObject(any(String.class), any())).thenReturn("{\"data\":{\"EUR\": 0.50}}");

        mvc.perform(MockMvcRequestBuilders.put("/accounts/transfer")
                        .param("senderId", "1")
                        .param("receiverId", "2")
                        .param("amount", "5"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void testAccountAudit() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/accounts/{accountId}/transactionHistory", 1)
                        .param("pageSize", "2")
                        .param("page", "1"))
                .andExpect(status().isOk());
    }
}
