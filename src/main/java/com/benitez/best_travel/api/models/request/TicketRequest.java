package com.benitez.best_travel.api.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketRequest {
    @Size(min = 18,max = 20, message = "The size have to a length between 18 and 20")
    @NotBlank(message = "id client is mandatory")
    private String idClient;
    @Positive
    @NotNull(message = "Id fly is mandatory")
    private Long idFly;
}
