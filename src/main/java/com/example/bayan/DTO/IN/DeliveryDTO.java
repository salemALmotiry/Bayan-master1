package com.example.bayan.DTO.IN;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    @NotEmpty(message = "carrier is required")
    private String carrier;

    @NotEmpty(message = "Tracking number is required")
    private String trackingNumber;

}
