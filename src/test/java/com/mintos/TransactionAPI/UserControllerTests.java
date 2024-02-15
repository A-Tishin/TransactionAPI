package com.mintos.TransactionAPI;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class UserControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    @Order(1)
    public void testUserCreation() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/user/create"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(2)
    public void testUserSearch() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @Order(3)
    public void testUserSearchNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/10"))
                .andExpect(status().isNotFound())
                .andReturn();

        String received = result.getResponse().getContentAsString();
        Assertions.assertEquals("User with ID: 10 is not found", received);
    }
}
