package com.alphaeventos.alphaweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String enterpriseName;
    private String personalName;
    private String email;
    private int telephoneContact;
    private String address;
    private URL rrss;  // Redes Sociales

    @OneToMany(mappedBy = "user")
    private List<Event> events;

    @OneToMany(mappedBy = "user")
    private List<Billing> billings;

    @OneToMany(mappedBy = "user")
    private List<Contract> contracts;
}
