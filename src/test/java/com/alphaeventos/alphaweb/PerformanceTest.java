package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.models.Performance;
import com.alphaeventos.alphaweb.repository.ArtistCalendarRepository;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.repository.PerformanceRepository;
import com.alphaeventos.alphaweb.services.ArtistService;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import com.alphaeventos.alphaweb.services.PerformanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class PerformanceRepositoryTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistCalendarRepository artistCalendarRepository;

    @BeforeEach
    public void cleanup() {
        performanceRepository.deleteAll();
        artistCalendarRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void testSavePerformance() {
        Artist artist = new Artist();
        artist.setArtisticName("Test Artist");
        artist = artistRepository.save(artist);

        ArtistCalendar artistCalendar = new ArtistCalendar("Weekdays 9-5", artist);
        artistCalendar = artistCalendarRepository.save(artistCalendar);

        Performance performance = new Performance(LocalDateTime.now(), "Test Location", artistCalendar);
        Performance savedPerformance = performanceRepository.save(performance);

        assertNotNull(savedPerformance);
        assertNotNull(savedPerformance.getId());
        assertEquals("Test Location", savedPerformance.getLocation());
    }
}
@SpringBootTest
@Transactional
class PerformanceServiceTest {

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistCalendarService artistCalendarService;

    private Artist artist;
    private ArtistCalendar artistCalendar;

    @BeforeEach
    public void setup() throws MalformedURLException {
        artist = new Artist();
        artist.setArtisticName("Artist Name");
        artist.setPhotosVideos(new URL("http://example.com/videos"));
        artist.setPersonalInformation("Some personal information");
        artist.setRrss(new URL("http://example.com/social"));
        artist.setTechnicalRider("Technical requirements");
        artist = artistService.save(artist);

        artistCalendar = new ArtistCalendar();
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar.setArtist(artist);
        artistCalendar = artistCalendarService.save(artistCalendar);
    }

    @Test
    public void testSavePerformance() {
        assertNotNull(artist);
        assertNotNull(artistCalendar);

        Performance performance = new Performance();
        performance.setDate(LocalDateTime.now());
        performance.setLocation("Main Stage");
        performance.setArtistCalendar(artistCalendar);

        Performance savedPerformance = performanceService.save(performance);
        assertNotNull(savedPerformance);
        assertNotNull(savedPerformance.getId());
        assertEquals("Main Stage", savedPerformance.getLocation());
    }

    @Test
    public void testFindAllPerformances() {
        Performance performance = new Performance();
        performance.setDate(LocalDateTime.now());
        performance.setLocation("Main Stage");
        performance.setArtistCalendar(artistCalendar);
        performanceService.save(performance);

        List<Performance> performances = performanceService.findAll();
        assertNotNull(performances);
        assertFalse(performances.isEmpty());
    }

    @Test
    public void testFindPerformanceById() {
        Performance performance = new Performance();
        performance.setDate(LocalDateTime.now());
        performance.setLocation("Main Stage");
        performance.setArtistCalendar(artistCalendar);

        Performance savedPerformance = performanceService.save(performance);

        Optional<Performance> performanceOptional = performanceService.findById(savedPerformance.getId());
        assertTrue(performanceOptional.isPresent());
        assertEquals("Main Stage", performanceOptional.get().getLocation());
    }

    @Test
    public void testDeletePerformance() {
        Performance performance = new Performance();
        performance.setDate(LocalDateTime.now());
        performance.setLocation("Test Location");
        performance.setArtistCalendar(artistCalendar);

        Performance savedPerformance = performanceService.save(performance);

        performanceService.deleteById(savedPerformance.getId());

        Optional<Performance> deletedPerformanceOptional = performanceService.findById(savedPerformance.getId());
        assertFalse(deletedPerformanceOptional.isPresent());
    }
}

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