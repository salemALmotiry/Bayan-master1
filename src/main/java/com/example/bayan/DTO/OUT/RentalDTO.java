package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {

    private LocalDateTime startDateTime;

    private Integer numberOfOrder;

    private BigDecimal totalPrice;

    private BigDecimal supPrice;

    private String status;

}
