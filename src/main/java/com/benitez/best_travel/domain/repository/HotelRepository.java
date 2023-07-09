package com.benitez.best_travel.domain.repository;

import com.benitez.best_travel.domain.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Set<Hotel> findByPriceLessThan(BigDecimal price);

    Set<Hotel> findByPriceBetween(BigDecimal priceMin, BigDecimal priceMax);

    Set<Hotel> findByRatingGreaterThan(Integer priceMax);

    Optional<Hotel> findByReservationsId(UUID id);
}
