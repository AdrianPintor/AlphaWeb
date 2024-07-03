package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.services.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getAllArtists() {
        List<Artist> artists = artistService.findAll();
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        Artist artist = artistService.findById(id).orElse(null);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artist);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist savedArtist = artistService.save(artist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        Artist existingArtist = artistService.findById(id).orElse(null);
        if (existingArtist == null) {
            return ResponseEntity.notFound().build();
        }
        // Update the existing artist with new information
        existingArtist.setArtisticName(artist.getArtisticName());
        existingArtist.setPhotosVideos(artist.getPhotosVideos());
        existingArtist.setPersonalInformation(artist.getPersonalInformation());
        existingArtist.setRrss(artist.getRrss());
        existingArtist.setTechnicalRider(artist.getTechnicalRider());

        // Save the updated artist
        Artist updatedArtist = artistService.save(existingArtist);
        return ResponseEntity.ok(updatedArtist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        Artist artist = artistService.findById(id).orElse(null);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        artistService.delete(artist);
        return ResponseEntity.noContent().build();
    }
}