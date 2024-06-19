package com.alphaeventos.alphaweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dniCif;
    private String billingAddress;

    @Enumerated(EnumType.STRING)
    private Bill billstatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
