package com.benitez.best_travel.api.controllers;

import com.benitez.best_travel.api.models.responses.HotelResponse;
import com.benitez.best_travel.infraestructure.abstract_services.IHotelService;
import com.benitez.best_travel.util.exceptions.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/hotels")
public class HotelController {
    private IHotelService hotelService;

    @GetMapping
    public ResponseEntity<Page<HotelResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType) {
        if (Objects.nonNull(sortType)) sortType = SortType.NONE;
        var response = hotelService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(
            @RequestParam BigDecimal price) {
        var response = hotelService.readLessPrice(price);
        return ResponseEntity.ok(response);
    }

    @GetMapping("between_price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        var response = hotelService.readBetweenPrices(min, max);
        return ResponseEntity.ok(response);
    }

    @GetMapping("rating")
    public ResponseEntity<Set<HotelResponse>> getGreaterThan(
            @RequestParam Integer rating) {
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;
        var response = hotelService.readGreaterThan(rating);
        return ResponseEntity.ok(response);
    }
}
