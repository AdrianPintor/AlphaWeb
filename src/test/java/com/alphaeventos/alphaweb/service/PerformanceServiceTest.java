package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.models.Performance;
import com.alphaeventos.alphaweb.services.ArtistService;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import com.alphaeventos.alphaweb.services.PerformanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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