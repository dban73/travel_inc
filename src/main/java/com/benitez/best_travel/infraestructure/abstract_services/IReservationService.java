package com.benitez.best_travel.infraestructure.abstract_services;

import com.benitez.best_travel.api.models.request.ReservationRequest;
import com.benitez.best_travel.api.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface IReservationService extends CrudInterface<ReservationRequest, ReservationResponse, UUID> {
    BigDecimal findByPrice(Long idHotel);
}
