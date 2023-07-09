package com.benitez.best_travel.api.models.responses;

import com.benitez.best_travel.util.exceptions.enums.AeroLine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FlyResponse {
    private long id;
    private double originLat;
    private double originLng;
    private double destinyLat;
    private double destinyLng;
    private String originName;
    private String destinyName;
    private BigDecimal price;
    private AeroLine aeroLine;
}
