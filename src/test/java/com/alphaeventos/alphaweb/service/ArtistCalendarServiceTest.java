package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import com.alphaeventos.alphaweb.services.ArtistService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.net.MalformedURLException;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArtistCalendarServiceTest {

    @Autowired
    private ArtistCalendarService artistCalendarService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;
    private Artist artist;
    private ArtistCalendar artistCalendar;

    @BeforeEach
    void setUp() throws MalformedURLException {
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
        artistCalendarService.save(artistCalendar);
    }

    @AfterEach
    void tearDown() {
        artistCalendarService.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void testSaveArtistCalendar() {
        Optional<Artist> artistOptional = artistService.findAll().stream().findFirst();
        assertTrue(artistOptional.isPresent(), "No artist found in the repository");
        Artist artist = artistOptional.get();

        ArtistCalendar artistCalendar = new ArtistCalendar();
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar.setArtist(artist);

        ArtistCalendar savedArtistCalendar = artistCalendarService.save(artistCalendar);
        assertNotNull(savedArtistCalendar);
        assertEquals("Weekdays 9-5", savedArtistCalendar.getAvailabilitySchedule());
    }

    @Test
    public void testFindById() {
        Optional<ArtistCalendar> artistCalendar = artistCalendarService.findById(1L);
        assertNotNull(artistCalendar);
    }

    @Test
    public void testFindAll() {
        artistCalendarService.save(artistCalendar);
        List<ArtistCalendar> artistCalendar = artistCalendarService.findAll();
        assertNotNull(artistCalendar.get(0));
        assertFalse(artistCalendar.isEmpty());
    }
}
