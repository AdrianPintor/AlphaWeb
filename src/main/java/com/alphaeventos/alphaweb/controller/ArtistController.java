package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.Artist;
import com.alphaeventos.alphaweb.services.ArtistService;
import jakarta.validation.Valid;
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
        return new ResponseEntity<>(artists, HttpStatus.OK);
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
    public Artist createArtist(@Valid @RequestBody Artist artist) {
        return artistService.save(artist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        Artist existingArtist = artistService.findById(id).orElse(null);
        if (existingArtist == null) {
            return ResponseEntity.notFound().build();
        }
        existingArtist.setArtisticName(artist.getArtisticName());
        existingArtist.setPhotosVideos(artist.getPhotosVideos());
        existingArtist.setPersonalInformation(artist.getPersonalInformation());
        existingArtist.setRrss(artist.getRrss());
        existingArtist.setTechnicalRider(artist.getTechnicalRider());

        Artist updatedArtist = artistService.save(existingArtist);
        return ResponseEntity.ok(updatedArtist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        Artist artist = artistService.findById(id).orElse(null);
        if (artist == null) {
            return ResponseEntity.notFound().build();
        }
        artistService.deleteById(artist.getId());
        return ResponseEntity.noContent().build();
    }
}