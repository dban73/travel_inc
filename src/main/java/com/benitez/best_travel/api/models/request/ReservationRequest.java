package com.benitez.best_travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationRequest {
    @Size(min = 18, max = 20, message = "the size have to a length between 18 and 20")
    @NotBlank(message = "id client is mandatory")
    private String idClient;
    @Positive
    @NotNull(message = "id hotel is mandatory")
    private Long idHotel;
    @Min(value = 1, message = "Min one days to make reservation")
    @Max(value = 30, message = "Max thirty days to make a reservation")
    @NotNull(message = "total days is mandatory")
    private Integer totalDays;
    //    @Pattern(regexp = "^(.+)@(.+)$")
    @Email(message = "Invalid Email")
    private String email;
}
