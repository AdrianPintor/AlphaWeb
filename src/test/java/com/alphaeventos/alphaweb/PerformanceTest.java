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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PerformanceRepositoryTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistCalendarRepository artistCalendarRepository;

    @Test
    public void testSavePerformance() {
        Optional<Artist> artistOptional = artistRepository.findById(1L);
        assertTrue(artistOptional.isPresent(), "Artist should be present");

        Artist artist = artistOptional.get();
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

    @BeforeEach
    public void setup() {
        artist = new Artist();
        artist.setArtisticName("Test Artist");
        artist = artistService.save(artist);

        ArtistCalendar artistCalendar = new ArtistCalendar();
        artistCalendar.setArtist(artist);
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar = artistCalendarService.save(artistCalendar);
    }

    @Test
    public void testSavePerformance() {
        Optional<ArtistCalendar> artistCalendarOptional = artistCalendarService.findById(1L);
        assertTrue(artistCalendarOptional.isPresent(), "ArtistCalendar should be present");

        ArtistCalendar artistCalendar = artistCalendarOptional.get();

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
        List<Performance> performances = performanceService.findAll();
        assertNotNull(performances);
        assertFalse(performances.isEmpty());
    }

    @Test
    public void testFindPerformanceById() {
        Performance performance = new Performance();
        performance.setDate(LocalDateTime.now());
        performance.setLocation("Main Stage");

        ArtistCalendar artistCalendar = new ArtistCalendar();
        artistCalendar.setArtist(artist);
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar = artistCalendarService.save(artistCalendar);

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

        ArtistCalendar artistCalendar = new ArtistCalendar();
        artistCalendar.setArtist(artist);
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar = artistCalendarService.save(artistCalendar);

        performance.setArtistCalendar(artistCalendar);
        Performance savedPerformance = performanceService.save(performance);

        performanceService.delete(savedPerformance.getId());

        Optional<Performance> deletedPerformanceOptional = performanceService.findById(savedPerformance.getId());
        assertFalse(deletedPerformanceOptional.isPresent());
    }
}
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PerformanceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private PerformanceService performanceService;

    private Artist artist;
    private ArtistCalendar artistCalendar;
    @Autowired
    private ArtistCalendarService artistCalendarService;

    @BeforeEach
    public void setup() throws MalformedURLException {
        artist = new Artist();
        artist.setArtisticName("Artist Name");
        artist.setPhotosVideos(new URL("http://example.com/videos"));
        artist.setPersonalInformation("Some personal information");
        artist.setRrss(new URL("http://example.com/social"));
        artist.setTechnicalRider("Technical requirements");
        artist = artistService.save(artist);

        artistCalendar = new ArtistCalendar("Weekdays 9-5", artist);
        artistCalendar = artistCalendarService.save(artistCalendar);
    }

    @Test
    public void testGetAllPerformances() {
        ResponseEntity<List> response = restTemplate.getForEntity("/performances", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Performance> performances = response.getBody();
        assertNotNull(performances);
    }

    @Test
    public void testGetPerformanceById() {
        Performance performance = new Performance(LocalDateTime.now(), "Main Stage", artistCalendar);
        ResponseEntity<Performance> createResponse = restTemplate.postForEntity("/performances", performance, Performance.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        Performance createdPerformance = createResponse.getBody();
        assertNotNull(createdPerformance);

        ResponseEntity<Performance> getResponse = restTemplate.getForEntity("/performances/" + createdPerformance.getId(), Performance.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        Performance fetchedPerformance = getResponse.getBody();
        assertNotNull(fetchedPerformance);
        assertEquals("Main Stage", fetchedPerformance.getLocation());
    }

    @Test
    public void testCreatePerformance() {
        Performance performance = new Performance(LocalDateTime.now(), "Main Stage", artistCalendar);
        ResponseEntity<Performance> response = restTemplate.postForEntity("/performances", performance, Performance.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Performance createdPerformance = response.getBody();
        assertNotNull(createdPerformance.getId());
        assertEquals("Main Stage", createdPerformance.getLocation());
    }
}