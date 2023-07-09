package com.benitez.best_travel.api.controllers;

import com.benitez.best_travel.api.models.responses.HotelResponse;
import com.benitez.best_travel.infraestructure.abstract_services.IHotelService;
import com.benitez.best_travel.util.exceptions.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Hotel")
public class HotelController {
    private IHotelService hotelService;

    @Operation(summary = "Return a page with hotels can be sorted or not")
    @GetMapping
    public ResponseEntity<Page<HotelResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType) {
        if (Objects.nonNull(sortType)) sortType = SortType.NONE;
        var response = hotelService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with hotels with price less to price in parameter")
    @GetMapping("less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(
            @RequestParam BigDecimal price) {
        var response = hotelService.readLessPrice(price);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with hotels with between prices in parameters")
    @GetMapping("between_price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        var response = hotelService.readBetweenPrices(min, max);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with hotels with ratting greater a parameter")
    @GetMapping("rating")
    public ResponseEntity<Set<HotelResponse>> getGreaterThan(
            @RequestParam Integer rating) {
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;
        var response = hotelService.readGreaterThan(rating);
        return ResponseEntity.ok(response);
    }
}
