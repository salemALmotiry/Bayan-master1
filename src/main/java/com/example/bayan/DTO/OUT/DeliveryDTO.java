package com.example.bayan.DTO.OUT;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    private String carrier;

    private String trackingNumber;

    private String status;




}
