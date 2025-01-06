package com.example.bayan.DTO.IN.Offer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {


    @NotNull
    private Integer postId;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;


}
