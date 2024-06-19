package com.alphaeventos.alphaweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String information;
    private String photosVideos;
    private String enterpriseCollabs;
    private String descriptionRequest;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "event")
    private List<Contract> contracts;

    @OneToMany(mappedBy = "event")
    private List<Offer> offers;
}
