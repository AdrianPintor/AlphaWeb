package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Bill;
import com.alphaeventos.alphaweb.models.Billing;
import com.alphaeventos.alphaweb.models.User;
import com.alphaeventos.alphaweb.repository.BillingRepository;
import com.alphaeventos.alphaweb.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class BillingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Billing billing;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User();
        user.setUsername("testuser");
        userRepository.save(user);

        billing = new Billing();
        billing.setDniCif("12345678A");
        billing.setBillingAddress("123 Main St");
        billing.setBillstatus(Bill.PAID);
        billing.setUser(user);
        billingRepository.save(billing);
    }

    @AfterEach
    void tearDown() {
        billingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllBillings() throws Exception {
        MvcResult result = mockMvc.perform(get("/billings"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("123 Main St"));
    }

    @Test
    void testGetBillingById() throws Exception {
        MvcResult result = mockMvc.perform(get("/billings/{id}", billing.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("12345678A"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateBilling() throws Exception {
        Billing newBilling = new Billing();
        newBilling.setDniCif("87654321B");
        newBilling.setBillingAddress("456 Elm St");
        newBilling.setBillstatus(Bill.UNPAID);
        newBilling.setUser(user);
        String body = objectMapper.writeValueAsString(newBilling);

        MvcResult result = mockMvc.perform(post("/billings")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("456 Elm St"));
    }

    @Test
    void testUpdateBilling() throws Exception {
        billing.setBillingAddress("Updated Address");
        String body = objectMapper.writeValueAsString(billing);

        mockMvc.perform(put("/billings/{id}", billing.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Billing updated = billingRepository.findById(billing.getId()).get();
        assertEquals("Updated Address", updated.getBillingAddress());
    }

    @Test
    void testDeleteBilling() throws Exception {
        mockMvc.perform(delete("/billings/{id}", billing.getId()))
                .andExpect(status().isNoContent());

        assertFalse(billingRepository.findById(billing.getId()).isPresent());
    }
}
