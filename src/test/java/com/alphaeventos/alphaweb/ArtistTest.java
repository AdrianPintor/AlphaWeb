package com.alphaeventos.alphaweb;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.services.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    private Artist artist;

    @BeforeEach
    public void setup() throws MalformedURLException {
        artist = new Artist();
        artist.setArtisticName("Artist Name");
        artist.setPhotosVideos(new URL("http://example.com/videos"));
        artist.setPersonalInformation("Some personal information");
        artist.setRrss(new URL("http://example.com/social"));
        artist.setTechnicalRider("Technical requirements");
    }

    @AfterEach
    public void teardown() {
        artistRepository.deleteAll();
    }

    @Test
    public void testCreateArtist() {
        Artist savedArtist = artistRepository.save(artist);
        assertNotNull(savedArtist.getId());
    }

    @Test
    public void testReadArtist() {
        Artist savedArtist = artistRepository.save(artist);
        Optional<Artist> fetchedArtist = artistRepository.findById(savedArtist.getId());
        assertTrue(fetchedArtist.isPresent());
        assertEquals("Artist Name", fetchedArtist.get().getArtisticName());
    }

    @Test
    public void testUpdateArtist() throws MalformedURLException {
        Artist savedArtist = artistRepository.save(artist);
        savedArtist.setArtisticName("Updated Name");
        savedArtist.setRrss(new URL("http://updated.com/social"));
        Artist updatedArtist = artistRepository.save(savedArtist);

        assertEquals("Updated Name", updatedArtist.getArtisticName());
        assertEquals("http://updated.com/social", updatedArtist.getRrss().toString());
    }

    @Test
    public void testDeleteArtist() {
        Artist savedArtist = artistRepository.save(artist);
        Long id = savedArtist.getId();
        artistRepository.deleteById(id);
        Optional<Artist> deletedArtist = artistRepository.findById(id);
        assertFalse(deletedArtist.isPresent());
    }

    @Test
    public void testListAllArtists() throws MalformedURLException {
        Artist anotherArtist = new Artist();
        anotherArtist.setArtisticName("Another Artist");
        anotherArtist.setPhotosVideos(new URL("http://another.com/videos"));
        anotherArtist.setPersonalInformation("More personal information");
        anotherArtist.setRrss(new URL("http://another.com/social"));
        anotherArtist.setTechnicalRider("Another technical requirements");

        artistRepository.save(artist);
        artistRepository.save(anotherArtist);

        List<Artist> artists = artistRepository.findAll();
        assertEquals(2, artists.size());
    }
}
@SpringBootTest
@Transactional
class ArtistServiceTest {

    @Autowired
    private ArtistService artistService;

    private Artist artist;
    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setup() throws MalformedURLException {
        artist = new Artist();
        artist.setArtisticName("Artist Name");
        artist.setPhotosVideos(new URL("http://example.com/videos"));
        artist.setPersonalInformation("Some personal information");
        artist.setRrss(new URL("http://example.com/social"));
        artist.setTechnicalRider("Technical requirements");
    }

    @Test
    public void testGetAllArtists() {
        artistRepository.save(artist);
        List<Artist> fetchedArtists = artistRepository.findAll();
        assertEquals("Aiko Tanaka", fetchedArtists.get(0).getArtisticName());
    }

    @Test
    public void testGetArtistById() {
        Artist savedArtist = artistService.save(artist);
        Optional<Artist> fetchedArtist = artistService.findById(savedArtist.getId());
        assertNotNull(fetchedArtist);
        assertEquals("Artist Name", fetchedArtist.get().getArtisticName());
    }

    @Test
    public void testCreateArtist() {
        Artist savedArtist = artistService.save(artist);
        assertNotNull(savedArtist.getId());
        assertEquals("Artist Name", savedArtist.getArtisticName());
    }

    @Test
    public void testUpdateArtist() {
        Artist savedArtist = artistService.save(artist);
        savedArtist.setArtisticName("Updated Name");
        Artist updatedArtist = artistService.save(savedArtist);
        assertEquals("Updated Name", updatedArtist.getArtisticName());
    }

    @Test
    public void testDeleteById() {
        Artist artist = new Artist();
        artist.setArtisticName("Artist Name");
        artist = artistService.save(artist);

        artistService.deleteById(artist.getId());
        assertFalse(artistService.findById(artist.getId()).isPresent());
    }
}
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