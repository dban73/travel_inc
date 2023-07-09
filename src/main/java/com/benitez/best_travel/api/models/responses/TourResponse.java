package com.benitez.best_travel.api.models.responses;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TourResponse {
    private long id;
    private Set<UUID> ticketsId;
    private Set<UUID> reservationIds;
}
