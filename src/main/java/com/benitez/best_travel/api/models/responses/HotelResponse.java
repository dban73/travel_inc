package com.benitez.best_travel.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HotelResponse {
    private long id;
    private String name;
    private String address;
    private int rating;
    private BigDecimal price;
}
