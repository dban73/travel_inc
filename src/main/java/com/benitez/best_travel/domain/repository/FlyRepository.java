package com.benitez.best_travel.domain.repository;

import com.benitez.best_travel.domain.entities.Fly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface FlyRepository extends JpaRepository<Fly, Long> {
    @Query("select f from fly f where f.price < :price")
    Set<Fly> selectLessPrice(BigDecimal price);

    @Query("select f from fly f where f.price between :min AND :max")
    Set<Fly> selectBetweenPrice(BigDecimal min, BigDecimal max);

    @Query("SELECT f FROM fly f WHERE f.originName = :origin AND f.destinyName = :destiny")
    Set<Fly> selectOriginDestiny(String origin, String destiny);

    @Query("SELECT f FROM fly f join fetch f.tickets t WHERE t.id = :id")
    Optional<Fly> findByTicketId(UUID id);
}
