package com.benitez.best_travel.infraestructure.Service;

import com.benitez.best_travel.api.models.request.TourRequest;
import com.benitez.best_travel.api.models.responses.TourResponse;
import com.benitez.best_travel.domain.entities.*;
import com.benitez.best_travel.domain.repository.CustomerRepository;
import com.benitez.best_travel.domain.repository.FlyRepository;
import com.benitez.best_travel.domain.repository.HotelRepository;
import com.benitez.best_travel.domain.repository.TourRepository;
import com.benitez.best_travel.infraestructure.abstract_services.ITourService;
import com.benitez.best_travel.infraestructure.helpers.BlackListHelper;
import com.benitez.best_travel.infraestructure.helpers.CustomerHelper;
import com.benitez.best_travel.infraestructure.helpers.EmailHelper;
import com.benitez.best_travel.infraestructure.helpers.TourHelper;
import com.benitez.best_travel.util.exceptions.IdNotFoundException;
import com.benitez.best_travel.util.exceptions.enums.Tables;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class TourService implements ITourService {
    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;


    @Override
    public TourResponse create(TourRequest request) {
        blackListHelper.isInBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
                () -> new IdNotFoundException(Tables.customer.name())
        );
        var flights = new HashSet<Fly>();
        request.getFlights().forEach(fly -> flights.add(flyRepository
                .findById(fly.getId()).orElseThrow(
                        () -> new IdNotFoundException(Tables.fly.name())
                )));
        var hotels = new HashMap<Hotel, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(hotelRepository
                .findById(hotel.getId()).orElseThrow(
                        () -> new IdNotFoundException(Tables.hotel.name())
                ), hotel.getTotalDays()));
        var tourToCreate = Tour.builder()
                .tickets(tourHelper.createTicket(flights, customer))
                .reservations(tourHelper.createReservations(hotels, customer))
                .customer(customer)
                .build();
        var tourSaved = tourRepository.save(tourToCreate);
        this.customerHelper.increase(customer.getDni(), TourService.class);
        if(Objects.nonNull(request.getEmail()))this.emailHelper.sendMail(request.getEmail(),customer.getFullName(),Tables.tour.name());
        return TourResponse.builder()
                .reservationIds(tourSaved.getReservations()
                        .stream().map(Reservation::getId)
                        .collect(Collectors.toSet()))
                .ticketsId(tourSaved.getTickets()
                        .stream().map(Ticket::getId)
                        .collect(Collectors.toSet()))
                .id(tourSaved.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {

        var tourFromDB = tourRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException(Tables.tour.name())
        );
        return TourResponse.builder()
                .reservationIds(tourFromDB.getReservations()
                        .stream().map(Reservation::getId)
                        .collect(Collectors.toSet()))
                .ticketsId(tourFromDB.getTickets()
                        .stream().map(Ticket::getId)
                        .collect(Collectors.toSet()))
                .id(tourFromDB.getId())
                .build();
    }

    @Override
    public void delete(Long id) {
        var tourDelete = tourRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException(Tables.tour.name())
        );
        tourRepository.delete(tourDelete);
    }

    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        var tourToUpdate = tourRepository.findById(tourId).orElseThrow(
                () -> new IdNotFoundException(Tables.tour.name())
        );
        tourToUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourToUpdate);
    }

    @Override
    public UUID addTicket(Long tourId, Long flyId) {
        var tourToUpdate = tourRepository.findById(tourId).orElseThrow(
                () -> new IdNotFoundException(Tables.tour.name())
        );
        var fly = this.flyRepository.findById(flyId).orElseThrow(
                () -> new IdNotFoundException(Tables.fly.name())
        );
        var ticket = this.tourHelper.createTicket(fly, tourToUpdate.getCustomer());
        tourToUpdate.addTicket(ticket);
        this.tourRepository.save(tourToUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation(Long touId, UUID reservationId) {
        var tourToUpdate = this.tourRepository.findById(touId).orElseThrow(
                () -> new IdNotFoundException(Tables.tour.name())
        );
        tourToUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourToUpdate);
    }

    @Override
    public UUID addReservation(Long tourId, Long hotelId, Integer totalDays) {
        var tourToUpdate = this.tourRepository.findById(tourId).orElseThrow(
                () -> new IdNotFoundException(Tables.tour.name())
        );
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(
                () -> new IdNotFoundException(Tables.hotel.name())
        );
        var reservation = this.tourHelper.createReservation(hotel, tourToUpdate.getCustomer(), totalDays);
        tourToUpdate.addReservation(reservation);
        this.tourRepository.save(tourToUpdate);
        return reservation.getId();
    }
}
