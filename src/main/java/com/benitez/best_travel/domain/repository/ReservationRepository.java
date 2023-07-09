package com.benitez.best_travel.domain.repository;

import com.benitez.best_travel.domain.entities.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, UUID> {
}
