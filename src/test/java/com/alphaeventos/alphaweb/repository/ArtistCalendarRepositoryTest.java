package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArtistCalendarRepositoryTest {

    @Autowired
    private ArtistCalendarRepository artistCalendarRepository;

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
        artistCalendarRepository.save(artistCalendar);
    }

    @AfterEach
    void tearDown() {
        artistCalendarRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveArtistCalendar() {
        Optional<Artist> artistOptional = artistRepository.findAll().stream().findFirst();
        assertTrue(artistOptional.isPresent(), "No artist found in the repository");
        Artist artist = artistOptional.get();


        ArtistCalendar artistCalendar = new ArtistCalendar();
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar.setArtist(artist);
        ArtistCalendar savedArtistCalendar = artistCalendarRepository.save(artistCalendar);


        assertNotNull(savedArtistCalendar);
        assertEquals("Weekdays 9-5", savedArtistCalendar.getAvailabilitySchedule());


        Optional<ArtistCalendar> retrievedArtistCalendar = artistCalendarRepository.findById(savedArtistCalendar.getId());
        assertTrue(retrievedArtistCalendar.isPresent());
        assertEquals("Weekdays 9-5", retrievedArtistCalendar.get().getAvailabilitySchedule());
        assertEquals("Aiko Tanaka", retrievedArtistCalendar.get().getArtist().getArtisticName());
    }

    @Test
    public void testFindById() {
        ArtistCalendar artistCalendar = artistCalendarRepository.findById(1L).orElse(null);
        assertNotNull(artistCalendar.getId());
    }

    @Test
    public void testFindAll() {
        List<ArtistCalendar> artistCalendars = artistCalendarRepository.findAll();
        assertFalse(artistCalendars.isEmpty());
    }
}
