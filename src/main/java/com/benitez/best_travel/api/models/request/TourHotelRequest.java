package com.benitez.best_travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TourHotelRequest {
    @Size(min = 18, max = 20, message = "the size have to a length between 18 and 20")
    @NotBlank(message = "id client is mandatory")
    private Long id;
    @Min(value = 1, message = "Min one days to make reservation")
    @Max(value = 30, message = "Max thirty days to make a reservation")
    @NotNull(message = "total days is mandatory")
    private Integer totalDays;
}
