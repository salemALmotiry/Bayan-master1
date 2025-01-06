package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {


    private String status;

    private Boolean hasDelivery;

    private String brokerName;

    private String borderName;

    private BigDecimal shipmentPrice;
}
