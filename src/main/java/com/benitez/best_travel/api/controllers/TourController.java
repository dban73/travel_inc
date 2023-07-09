package com.benitez.best_travel.api.controllers;

import com.benitez.best_travel.api.models.request.TourRequest;
import com.benitez.best_travel.api.models.responses.ErrorResponse;
import com.benitez.best_travel.api.models.responses.TourResponse;
import com.benitez.best_travel.infraestructure.abstract_services.ITourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/tours")
@Tag(name = "Tour")
public class TourController {
    private final ITourService tourService;
    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )

    @Operation(summary = "Save in system one Tour based in list of hotels and flights")
    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest request) {
        return ResponseEntity.ok(tourService.create(request));
    }

    @Operation(summary = "Delete a Tour with ad passed")
    @GetMapping(path = "/{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.read(id));
    }

    @Operation(summary = "Remove a tour")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tourService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Remove a ticket from tour")
    @PatchMapping(path = "/{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long tourId,
                                             @PathVariable UUID ticketId) {
        this.tourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "add a ticket to tour")
    @PatchMapping(path = "/{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId,
                                                        @PathVariable Long flyId) {
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Remove a reservation from tour")
    @PatchMapping(path = "/{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long tourId,
                                                  @PathVariable UUID reservationId) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "add a reservation to tour")
    @PatchMapping(path = "/{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postReservation(@PathVariable Long tourId,
                                                             @PathVariable Long hotelId,
                                                             @RequestParam Integer totalDays) {
        var response = Collections.singletonMap("ticketId", this.tourService.addReservation(tourId, hotelId, totalDays));
        return ResponseEntity.ok(response);
    }
}
