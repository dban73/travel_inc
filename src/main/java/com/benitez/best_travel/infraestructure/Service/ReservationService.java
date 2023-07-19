package com.benitez.best_travel.infraestructure.Service;

import com.benitez.best_travel.api.models.request.ReservationRequest;
import com.benitez.best_travel.api.models.responses.HotelResponse;
import com.benitez.best_travel.api.models.responses.ReservationResponse;
import com.benitez.best_travel.domain.entities.Reservation;
import com.benitez.best_travel.domain.repository.CustomerRepository;
import com.benitez.best_travel.domain.repository.HotelRepository;
import com.benitez.best_travel.domain.repository.ReservationRepository;
import com.benitez.best_travel.infraestructure.abstract_services.IReservationService;
import com.benitez.best_travel.infraestructure.helpers.ApiCurrencyConnectionHelper;
import com.benitez.best_travel.infraestructure.helpers.BlackListHelper;
import com.benitez.best_travel.infraestructure.helpers.CustomerHelper;
import com.benitez.best_travel.infraestructure.helpers.EmailHelper;
import com.benitez.best_travel.util.exceptions.IdNotFoundException;
import com.benitez.best_travel.util.exceptions.enums.Tables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectionHelper currencyConnectionHelper;
    private final EmailHelper emailHelper;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(
                () -> new IdNotFoundException(Tables.hotel.name())
        );
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow(
                () -> new IdNotFoundException(Tables.customer.name())
        );
        var reservationToPersist = Reservation.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGE)))
                .build();
        var reservationPersisted = reservationRepository.save(reservationToPersist);
        this.customerHelper.increase(customer.getDni(), ReservationService.class);

        if(Objects.nonNull(request.getEmail()))this.emailHelper.sendMail(request.getEmail(),customer.getFullName(),Tables.reservation.name());
        return this.entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        var reservationFromDB = this.reservationRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException(Tables.reservation.name())
        );
        return this.entityToResponse(reservationFromDB);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID uuid) {
        var reservationToUpdate = reservationRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException(Tables.reservation.name())
        );
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(
                () -> new IdNotFoundException(Tables.hotel.name())
        );
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGE)));
        var reservationUpdated = reservationRepository.save(reservationToUpdate);
        log.info("Reservation updated with id {}", reservationUpdated.getId());
        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID uuid) {
        var reservationToDelete = reservationRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException(Tables.reservation.name())
        );
        this.reservationRepository.delete(reservationToDelete);
    }

    public static final BigDecimal CHARGES_PRICE_PERCENTAGE = BigDecimal.valueOf(0.20);

    private ReservationResponse entityToResponse(Reservation entity) {
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }


    @Override
    public BigDecimal findByPrice(Long idHotel, Currency currency) {
        var hotel = hotelRepository.findById(idHotel).orElseThrow(
                () -> new IdNotFoundException(Tables.hotel.name())
        );
        var priceInDollars = hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGE));
        if(currency.equals(Currency.getInstance("USD"))) return priceInDollars;
        var currencyDTO = this.currencyConnectionHelper.getCurrency(currency);
        log.info("API currency in {}, response {}",currencyDTO.getExchangeDate().toString(),
                currencyDTO.getRates());
        return priceInDollars.multiply(currencyDTO.getRates().get(currency));
    }
}
