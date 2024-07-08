package com.alphaeventos.alphaweb.service;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.services.ArtistService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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
