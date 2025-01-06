package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrokerRentalsDTO {

    private String fullName;

    private String phoneNumber;

    private String companyName;

    private List<RentalDTO> rentalDTOS;
}
