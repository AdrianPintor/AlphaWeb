package com.alphaeventos.alphaweb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCalendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String availabilitySchedule;

    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "artistCalendar")
    private List<Performance> performances;

    public ArtistCalendar(String availabilitySchedule, Artist artist) {
        this.availabilitySchedule = availabilitySchedule;
        this.artist = artist;
    }
}