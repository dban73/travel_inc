package com.benitez.best_travel.api.models.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TourFlyRequest {
    @Positive
    @NotNull
    private Long id;
}
