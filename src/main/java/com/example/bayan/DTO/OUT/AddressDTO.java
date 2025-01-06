package com.example.bayan.DTO.OUT;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String city;

    private String neighborhood;

    private String street;

    private String postalCode;

    private String buildingNumber;
}
