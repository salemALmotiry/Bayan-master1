package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRentalsDTO {

    private String fullName;

    private String email;

    private String phoneNumber;

    
    private List<RentalDTO>rentalDTOS;


}
