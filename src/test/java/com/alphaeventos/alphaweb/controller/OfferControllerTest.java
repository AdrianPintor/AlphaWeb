package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Event;
import com.alphaeventos.alphaweb.models.Offer;
import com.alphaeventos.alphaweb.repository.EventRepository;
import com.alphaeventos.alphaweb.repository.OfferRepository;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class OfferControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private EventRepository eventRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Offer offer;
    private Event event;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        event = new Event();
        event.setName("Test Event");
        eventRepository.save(event);

        offer = new Offer();
        offer.setDate(LocalDate.now());
        offer.setTerms("Test Terms");
        offer.setEvent(event);
        offerRepository.save(offer);
    }

    @AfterEach
    void tearDown() {
        offerRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    void testGetAllOffers() throws Exception {
        MvcResult result = mockMvc.perform(get("/offers"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Terms"));
    }

    @Test
    void testGetOfferById() throws Exception {
        MvcResult result = mockMvc.perform(get("/offers/{id}", offer.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Test Terms"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateOffer() throws Exception {
        Offer newOffer = new Offer();
        newOffer.setDate(LocalDate.now());
        newOffer.setTerms("New Terms");
        newOffer.setEvent(event);
        String body = objectMapper.writeValueAsString(newOffer);

        MvcResult result = mockMvc.perform(post("/offers")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Terms"));
    }

    @Test
    void testUpdateOffer() throws Exception {
        offer.setTerms("Updated Terms");
        String body = objectMapper.writeValueAsString(offer);

        mockMvc.perform(put("/offers/{id}", offer.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Offer updated = offerRepository.findById(offer.getId()).get();
        assertEquals("Updated Terms", updated.getTerms());
    }

    @Test
    void testDeleteOffer() throws Exception {
        mockMvc.perform(delete("/offers/{id}", offer.getId()))
                .andExpect(status().isNoContent());

        assertFalse(offerRepository.findById(offer.getId()).isPresent());
    }
}