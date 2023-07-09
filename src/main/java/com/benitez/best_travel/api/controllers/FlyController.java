package com.benitez.best_travel.api.controllers;

import com.benitez.best_travel.api.models.responses.FlyResponse;
import com.benitez.best_travel.infraestructure.abstract_services.IFlyService;
import com.benitez.best_travel.util.exceptions.enums.SortType;
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
@RequestMapping(path = "/flights")
@Tag(name = "Fly")
public class FlyController {
    private final IFlyService flyService;

    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType) {
        if (Objects.nonNull(sortType)) sortType = SortType.NONE;
        var response = flyService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping("less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(
            @RequestParam BigDecimal price) {
        var response = flyService.readLessPrice(price);
        return ResponseEntity.ok(response);
    }

    @GetMapping("between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        var response = flyService.readBetweenPrices(min, max);
        return ResponseEntity.ok(response);
    }

    @GetMapping("origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getOriginDestiny(
            @RequestParam String origin,
            @RequestParam String destiny) {
        var response = flyService.readByOriginDestiny(origin, destiny);
        return ResponseEntity.ok(response);
    }
}
