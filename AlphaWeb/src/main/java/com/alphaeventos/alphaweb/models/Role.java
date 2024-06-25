package com.alphaeventos.alphaweb.models;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}