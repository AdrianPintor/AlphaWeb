package com.alphaeventos.alphaweb;

import com.alphaeventos.alphaweb.controller.ArtistCalendarController;
import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.repository.ArtistCalendarRepository;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import com.alphaeventos.alphaweb.services.ArtistService;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        assertNotNull(artistCalendar);
    }

    @Test
    public void testFindAll() {
        List<ArtistCalendar> artistCalendars = artistCalendarRepository.findAll();
        assertFalse(artistCalendars.isEmpty());
    }
}

@SpringBootTest
@Transactional
class ArtistCalendarServiceTest {

    @Autowired
    private ArtistCalendarService artistCalendarService;

    @Autowired
    private ArtistService artistService;

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
        List<ArtistCalendar> artistCalendars = artistCalendarService.findAll();
        assertFalse(artistCalendars.isEmpty());
    }
}

@SpringBootTest
@Transactional
class ArtistCalendarControllerTest {

    @Autowired
    private ArtistCalendarController artistCalendarController;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @Transactional
    public void testCreateArtistCalendar() {
        Artist artist = artistRepository.findAll().stream()
                .filter(a -> a.getArtistCalendar() == null)
                .findFirst()
                .orElseGet(() -> {
                    fail("No artist without an ArtistCalendar found in the repository");
                    return null;
                });

        ArtistCalendar artistCalendar = new ArtistCalendar();
        artistCalendar.setAvailabilitySchedule("Weekdays 9-5");
        artistCalendar.setArtist(artist);


        ResponseEntity<ArtistCalendar> response = artistCalendarController.createArtistCalendar(artistCalendar);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Weekdays 9-5", response.getBody().getAvailabilitySchedule());
    }

    @Test
    public void testGetArtistCalendarById() {
        ResponseEntity<ArtistCalendar> response = artistCalendarController.getArtistCalendarById(1L);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAllArtistCalendars() {
        ResponseEntity<List<ArtistCalendar>> response = artistCalendarController.getAllArtistCalendars();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}