package com.alphaeventos.alphaweb.models;

import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String artisticName;
    private URL photosVideos;
    private String personalInformation;
    private URL rrss;  // Redes Sociales
    private String technicalRider;

    @OneToOne(mappedBy = "artist")
    private ArtistCalendar artistCalendar;

    @OneToMany(mappedBy = "artist")
    private List<Contract> contracts;
}