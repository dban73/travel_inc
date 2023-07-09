package com.benitez.best_travel.infraestructure.helpers;

import com.benitez.best_travel.domain.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@AllArgsConstructor
public class CustomerHelper {
    private final CustomerRepository customerRepository;

    public void increase(String customerId, Class<?> type) {
        var customerToUpdate = this.customerRepository.findById(customerId).orElseThrow();
        switch (type.getSimpleName()) {
            case "TourService" -> customerToUpdate.setTotalTours(customerToUpdate.getTotalTours() + 1);
            case "TicketService" -> customerToUpdate.setTotalFlights(customerToUpdate.getTotalFlights() + 1);
            case "ReservationService" -> customerToUpdate.setTotalLandings(customerToUpdate.getTotalLandings() + 1);
        }
        this.customerRepository.save(customerToUpdate);
    }

    public void decrease(String customerId, Class<?> type) {
        var customerToUpdate = this.customerRepository.findById(customerId).orElseThrow();
        switch (type.getSimpleName()) {
            case "TourService" -> customerToUpdate.setTotalTours(customerToUpdate.getTotalTours() - 1);
            case "TicketService" -> customerToUpdate.setTotalFlights(customerToUpdate.getTotalFlights() - 1);
            case "ReservationService" -> customerToUpdate.setTotalLandings(customerToUpdate.getTotalLandings() - 1);
        }
    }
}
