package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/artist-calendars")
public class ArtistCalendarController {

    private final ArtistCalendarService artistCalendarService;

    @Autowired
    public ArtistCalendarController(ArtistCalendarService artistCalendarService) {
        this.artistCalendarService = artistCalendarService;
    }

    @GetMapping
    public ResponseEntity<List<ArtistCalendar>> getAllArtistCalendars() {
        List<ArtistCalendar> artistCalendars = artistCalendarService.findAll();
        return ResponseEntity.ok(artistCalendars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistCalendar> getArtistCalendarById(@PathVariable Long id) {
        Optional<ArtistCalendar> artistCalendar = artistCalendarService.findById(id);
        return artistCalendar.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtistCalendar> createArtistCalendar(@RequestBody ArtistCalendar artistCalendar) {
        ArtistCalendar savedArtistCalendar = artistCalendarService.save(artistCalendar);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArtistCalendar);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtistCalendar> updateArtistCalendar(@PathVariable Long id, @RequestBody ArtistCalendar artistCalendar) {
        artistCalendar.setId(id);
        ArtistCalendar updatedArtistCalendar = artistCalendarService.save(artistCalendar);
        return ResponseEntity.ok(updatedArtistCalendar);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteArtistCalendar(@PathVariable Long id) {
        artistCalendarService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}