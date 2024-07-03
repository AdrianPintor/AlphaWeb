package com.alphaeventos.alphaweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private String location;

    @ManyToOne
    @JoinColumn(name = "artist_calendar_id")
    private ArtistCalendar artistCalendar;

    public Performance(LocalDateTime date, String location, ArtistCalendar artistCalendar) {
        this.date = date;
        this.location = location;
        this.artistCalendar = artistCalendar;
    }
}