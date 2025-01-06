package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOfferDTO {

    private String fullName;

    private  String CompanyName;

    private BigDecimal price;

}
