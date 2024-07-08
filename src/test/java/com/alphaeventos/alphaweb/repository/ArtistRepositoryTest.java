package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.Artist;
import org.junit.jupiter.api.AfterEach;
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