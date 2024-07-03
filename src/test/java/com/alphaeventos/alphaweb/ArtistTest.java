package com.alphaeventos.alphaweb;

import static org.junit.jupiter.api.Assertions.*;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.repository.ArtistRepository;
import com.alphaeventos.alphaweb.services.ArtistService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


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
        artistService.save(artist);
        List<Artist> fetchedArtists = artistService.findAll();
        assertEquals(4, fetchedArtists.size());
        assertEquals("Artist Name", fetchedArtists.get(0).getArtisticName());
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArtistControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
    @Test
    public void testGetAllArtists() {
        ResponseEntity<List> response = restTemplate.getForEntity("/artists", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Artist> artists = response.getBody();
        assertNotNull(artists);
    }
    @Test
    public void testGetArtistById() {
        // Create an artist first
        ResponseEntity<Artist> createResponse = restTemplate.postForEntity("/artists", artist, Artist.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Artist createdArtist = createResponse.getBody();
        assertNotNull(createdArtist);

        // Get the artist by ID
        ResponseEntity<Artist> getResponse = restTemplate.getForEntity("/artists/" + createdArtist.getId(), Artist.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Artist fetchedArtist = getResponse.getBody();
        assertNotNull(fetchedArtist);
        assertEquals("Artist Name", fetchedArtist.getArtisticName());
    }
    @Test
    public void testCreateArtist() {
        ResponseEntity<Artist> response = restTemplate.postForEntity("/artists", artist, Artist.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Artist createdArtist = response.getBody();
        assertNotNull(createdArtist.getId());
        assertEquals("Artist Name", createdArtist.getArtisticName());
    }
}