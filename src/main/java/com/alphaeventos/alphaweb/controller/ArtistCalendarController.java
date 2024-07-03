package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Optional<ArtistCalendar> artistCalendarOptional = artistCalendarService.findById(id);
        return artistCalendarOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistCalendar> createArtistCalendar(@RequestBody ArtistCalendar artistCalendar) {
        ArtistCalendar savedArtistCalendar = artistCalendarService.save(artistCalendar);
        return new ResponseEntity<>(savedArtistCalendar, HttpStatus.CREATED);
    }
}