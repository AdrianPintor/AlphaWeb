package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.models.Performance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
