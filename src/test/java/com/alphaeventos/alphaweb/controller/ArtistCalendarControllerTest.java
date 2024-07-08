package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.repository.ArtistCalendarRepository;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
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

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class ArtistCalendarControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArtistCalendarRepository artistCalendarRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private ArtistCalendar artistCalendar;
    private Artist artist;

    @BeforeEach
    void setUp() throws MalformedURLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        artist = new Artist();
        artist.setArtisticName("Test Artist");
        artist.setPhotosVideos(new URL("http://example.com/videos"));
        artist.setPersonalInformation("Some personal information");
        artist.setRrss(new URL("http://example.com/social"));
        artist.setTechnicalRider("Technical requirements");
        artistRepository.save(artist);

        artistCalendar = new ArtistCalendar();
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar.setArtist(artist);
        artistCalendarRepository.save(artistCalendar);
    }

    @AfterEach
    void tearDown() {
        artistCalendarRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void testGetAllArtistCalendars() throws Exception {
        MvcResult result = mockMvc.perform(get("/artist-calendars"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Weekdays 9-5"));
    }

    @Test
    void testGetArtistCalendarById() throws Exception {
        MvcResult result = mockMvc.perform(get("/artist-calendars/{id}", artistCalendar.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Weekdays 9-5"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateArtistCalendar() throws Exception {
        Artist newArtist = new Artist();
        newArtist.setArtisticName("New Artist");
        newArtist.setPhotosVideos(new URL("http://example.com/new-videos"));
        newArtist.setPersonalInformation("Some new personal information");
        newArtist.setRrss(new URL("http://example.com/new-social"));
        newArtist.setTechnicalRider("New technical requirements");
        artistRepository.save(newArtist);

        ArtistCalendar newArtistCalendar = new ArtistCalendar();
        newArtistCalendar.setAvailabilitySchedule("Weekends 10-4");
        newArtistCalendar.setArtist(newArtist);
        String body = objectMapper.writeValueAsString(newArtistCalendar);

        MvcResult result = mockMvc.perform(post("/artist-calendars")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Weekends 10-4"));
    }

    @Test
    void testUpdateArtistCalendar() throws Exception {
        artistCalendar.setAvailabilitySchedule("Updated Schedule");
        String body = objectMapper.writeValueAsString(artistCalendar);

        mockMvc.perform(put("/artist-calendars/{id}", artistCalendar.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArtistCalendar updated = artistCalendarRepository.findById(artistCalendar.getId()).get();
        assertEquals("Updated Schedule", updated.getAvailabilitySchedule());
    }

    @Test
    void testDeleteArtistCalendar() throws Exception {
        mockMvc.perform(delete("/artist-calendars/{id}", artistCalendar.getId()))
                .andExpect(status().isNoContent());

        assertFalse(artistCalendarRepository.findById(artistCalendar.getId()).isPresent());
    }
}
