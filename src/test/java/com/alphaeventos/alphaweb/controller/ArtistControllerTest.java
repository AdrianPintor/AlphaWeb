package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Artist;
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
class ArtistControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArtistRepository artistRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Artist artist;

    @BeforeEach
    void setUp() throws MalformedURLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        artist = new Artist();
        artist.setArtisticName("Artist Name");
        artist.setPhotosVideos(new URL("http://example.com/videos"));
        artist.setPersonalInformation("Some personal information");
        artist.setRrss(new URL("http://example.com/social"));
        artist.setTechnicalRider("Technical requirements");
        artistRepository.save(artist);
    }

    @AfterEach
    void tearDown() {
        artistRepository.deleteAll();
    }

    @Test
    void testGetAllArtist() throws Exception {
        MvcResult result = mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Artist Name"));
    }

    @Test
    void testGetArtistById() throws Exception {
        MvcResult result = mockMvc.perform(get("/artists/{id}", artist.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Artist Name"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateArtist() throws Exception {
        Artist newArtist = new Artist();
        newArtist.setArtisticName("New Artist Name");
        newArtist.setPhotosVideos(new URL("http://example.com/videos"));
        newArtist.setPersonalInformation("Some personal information");
        newArtist.setRrss(new URL("http://example.com/social"));
        newArtist.setTechnicalRider("Technical requirements");
        String body = objectMapper.writeValueAsString(newArtist);

        MvcResult result = mockMvc.perform(post("/artists")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("New Artist Name"));
    }

    @Test
    void testUpdateUserArtist() throws Exception {
        artist.setArtisticName("Updated Artist Name");
        artist.setTechnicalRider("Technical requirements");
        String body = objectMapper.writeValueAsString(artist);

        mockMvc.perform(put("/artists/{id}", artist.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Artist updated = artistRepository.findById(artist.getId()).get();
        assertEquals("Updated Artist Name", updated.getArtisticName());
        assertEquals("Technical requirements", updated.getTechnicalRider());
    }

    @Test
    void testDeleteArtist() throws Exception {
        mockMvc.perform(delete("/artists/{id}", artist.getId()))
                .andExpect(status().isNoContent());

        assertFalse(artistRepository.findById(artist.getId()).isPresent());
    }
}
