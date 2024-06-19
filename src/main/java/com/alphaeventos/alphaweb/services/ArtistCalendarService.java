package com.alphaeventos.alphaweb.services;

import com.alphaeventos.alphaweb.models.ArtistCalendar;
import com.alphaeventos.alphaweb.repository.ArtistCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistCalendarService {

    @Autowired
    private ArtistCalendarRepository artistCalendarRepository;

    public List<ArtistCalendar> findAll() {
        return artistCalendarRepository.findAll();
    }

    public Optional<ArtistCalendar> findById(Long id) {
        return artistCalendarRepository.findById(id);
    }

    public ArtistCalendar save(ArtistCalendar artistCalendar) {
        return artistCalendarRepository.save(artistCalendar);
    }

    public void deleteById(Long id) {
        artistCalendarRepository.deleteById(id);
    }
}