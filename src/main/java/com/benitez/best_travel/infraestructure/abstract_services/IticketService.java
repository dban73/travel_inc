package com.benitez.best_travel.infraestructure.abstract_services;

import com.benitez.best_travel.api.models.request.TicketRequest;
import com.benitez.best_travel.api.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface IticketService extends CrudInterface<TicketRequest, TicketResponse, UUID> {
    BigDecimal findPrice(Long flyId);
}
