package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.services.ArtistCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/artist-calendars")
public class ArtistCalendarController {

    @Autowired
    private ArtistCalendarService artistCalendarService;

    @GetMapping
    public List<ArtistCalendar> getAllArtistCalendars() {
        return artistCalendarService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<ArtistCalendar> getArtistCalendarById(@PathVariable Long id) {
        return artistCalendarService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArtistCalendar createArtistCalendar(@RequestBody ArtistCalendar artistCalendar) {
        return artistCalendarService.save(artistCalendar);
    }

    @PutMapping("/{id}")
    public ArtistCalendar updateArtistCalendar(@PathVariable Long id, @RequestBody ArtistCalendar artistCalendar) {
        return artistCalendarService.save(artistCalendar);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArtistCalendar(@PathVariable Long id) {
        artistCalendarService.deleteById(id);
    }
}
