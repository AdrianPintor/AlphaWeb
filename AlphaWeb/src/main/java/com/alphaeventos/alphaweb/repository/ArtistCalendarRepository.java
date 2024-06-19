package com.alphaeventos.alphaweb.repository;

import com.alphaeventos.alphaweb.models.ArtistCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistCalendarRepository extends JpaRepository<ArtistCalendar, Long> {
}
