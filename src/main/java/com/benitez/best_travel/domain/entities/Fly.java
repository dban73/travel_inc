package com.benitez.best_travel.domain.entities;

import com.benitez.best_travel.util.exceptions.enums.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "fly")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Fly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;
    private String originName;
    private String destinyName;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL
            , fetch = FetchType.EAGER
            , orphanRemoval = true
            , mappedBy = "fly")
    private Set<Ticket> tickets;
}
