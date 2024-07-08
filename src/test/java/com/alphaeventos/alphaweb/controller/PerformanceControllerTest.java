package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.models.Performance;
import com.alphaeventos.alphaweb.repository.ArtistCalendarRepository;
import com.alphaeventos.alphaweb.repository.PerformanceRepository;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class PerformanceControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ArtistCalendarRepository artistCalendarRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Performance performance;
    private ArtistCalendar artistCalendar;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        artistCalendar = new ArtistCalendar();
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar = artistCalendarRepository.save(artistCalendar);

        performance = new Performance();
        performance.setDate(LocalDateTime.now());
        performance.setLocation("Main Stage");
        performance.setArtistCalendar(artistCalendar);
        performance = performanceRepository.save(performance);
    }

    @AfterEach
    void tearDown() {
        performanceRepository.deleteAll();
        artistCalendarRepository.deleteAll();
    }

    @Test
    void testGetAllPerformances() throws Exception {
        MvcResult result = mockMvc.perform(get("/performances"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Main Stage"));
    }

    @Test
    void testGetPerformanceById() throws Exception {
        MvcResult result = mockMvc.perform(get("/performances/{id}", performance.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Main Stage"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreatePerformance() throws Exception {
        Performance newPerformance = new Performance();
        newPerformance.setDate(LocalDateTime.now());
        newPerformance.setLocation("New Location");
        newPerformance.setArtistCalendar(artistCalendar);
        String body = objectMapper.writeValueAsString(newPerformance);

        MvcResult result = mockMvc.perform(post("/performances")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Location"));
    }

    @Test
    void testUpdatePerformance() throws Exception {
        performance.setLocation("Updated Location");
        String body = objectMapper.writeValueAsString(performance);

        mockMvc.perform(put("/performances/{id}", performance.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Performance updated = performanceRepository.findById(performance.getId()).get();
        assertEquals("Updated Location", updated.getLocation());
    }

    @Test
    void testDeletePerformance() throws Exception {
        mockMvc.perform(delete("/performances/{id}", performance.getId()))
                .andExpect(status().isNoContent());

        assertFalse(performanceRepository.findById(performance.getId()).isPresent());
    }
}
