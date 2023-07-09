package com.benitez.best_travel.infraestructure.Service;

import com.benitez.best_travel.api.models.request.TicketRequest;
import com.benitez.best_travel.api.models.responses.FlyResponse;
import com.benitez.best_travel.api.models.responses.TicketResponse;
import com.benitez.best_travel.domain.entities.Ticket;
import com.benitez.best_travel.domain.repository.CustomerRepository;
import com.benitez.best_travel.domain.repository.FlyRepository;
import com.benitez.best_travel.domain.repository.TicketRepository;
import com.benitez.best_travel.infraestructure.abstract_services.IticketService;
import com.benitez.best_travel.infraestructure.helpers.BlackListHelper;
import com.benitez.best_travel.infraestructure.helpers.CustomerHelper;
import com.benitez.best_travel.util.BestTravelUtil;
import com.benitez.best_travel.util.exceptions.IdNotFoundException;
import com.benitez.best_travel.util.exceptions.enums.Tables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class TicketService implements IticketService {
    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(
                () -> new IdNotFoundException(Tables.fly.name())
        );
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(
                () -> new IdNotFoundException(Tables.customer.name())
        );
        var ticketToPersist = Ticket.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();
        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        this.customerHelper.increase(customer.getDni(), TicketService.class);
        log.info("Ticket saved with id: {}", ticketPersisted.getId());
        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        var ticketFromDB = this.ticketRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException(Tables.ticket.name())
        );
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        var ticketToUpdate = ticketRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException(Tables.ticket.name())
        );
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(
                () -> new IdNotFoundException(Tables.fly.name())
        );
        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLater());
        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);
        log.info("Ticket updated id: {}", ticketUpdated.getId());
        return this.entityToResponse(ticketToUpdate);
    }

    @Override
    public void delete(UUID uuid) {
        var ticketToDelete = ticketRepository.findById(uuid).orElseThrow(
                () -> new IdNotFoundException(Tables.ticket.name())
        );
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly = this.flyRepository.findById(flyId).orElseThrow(
                () -> new IdNotFoundException(Tables.ticket.name())
        );
        return fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE));
    }

    public static final BigDecimal CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);

    private TicketResponse entityToResponse(Ticket entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        FlyResponse flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }
}
