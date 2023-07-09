package com.benitez.best_travel.domain.repository;

import com.benitez.best_travel.domain.entities.Tour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends CrudRepository<Tour, Long> {
}
