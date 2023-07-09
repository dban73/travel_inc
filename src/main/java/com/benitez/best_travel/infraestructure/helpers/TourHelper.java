package com.benitez.best_travel.infraestructure.helpers;

import com.benitez.best_travel.domain.entities.*;
import com.benitez.best_travel.domain.repository.ReservationRepository;
import com.benitez.best_travel.domain.repository.TicketRepository;
import com.benitez.best_travel.infraestructure.Service.ReservationService;
import com.benitez.best_travel.infraestructure.Service.TicketService;
import com.benitez.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Transactional
@Component
public class TourHelper {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<Ticket> createTicket(Set<Fly> flights, Customer customer) {
        var response = new HashSet<Ticket>(flights.size());
        flights.forEach(fly -> {
            var ticketToPersist = Ticket.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer(customer)
                    .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.CHARGER_PRICE_PERCENTAGE)))
                    .purchaseDate(LocalDate.now())
                    .arrivalDate(BestTravelUtil.getRandomLater())
                    .departureDate(BestTravelUtil.getRandomSoon())
                    .build();
            response.add(ticketRepository.save(ticketToPersist));
        });
        return response;
    }

    public Set<Reservation> createReservations(HashMap<Hotel, Integer> hotels, Customer customer) {
        var response = new HashSet<Reservation>(hotels.size());
        hotels.forEach((hotel, totalDays) -> {
            var reservationToPersist = Reservation.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer(customer)
                    .totalDays(totalDays)
                    .dateTimeReservation(LocalDateTime.now())
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(totalDays))
                    .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.CHARGES_PRICE_PERCENTAGE)))
                    .build();
            response.add(reservationRepository.save(reservationToPersist));
        });

        return response;
    }

    public Ticket createTicket(Fly fly, Customer customer) {
        var ticketToPersist = Ticket.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(ReservationService.CHARGES_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .build();
        return this.ticketRepository.save(ticketToPersist);
    }

    public Reservation createReservation(Hotel hotel, Customer customer, Integer totalDays) {
        var reservationToPersist = Reservation.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.CHARGES_PRICE_PERCENTAGE)))
                .build();
        return this.reservationRepository.save(reservationToPersist);
    }
}
