package com.example.bayan.DTO.OUT;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBrokerDTO {

    // username
    private String username;

    //email
    private String email;

    // phoneNumer
    private String phoneNumber;

    private String fullName;

    private String licenseNumber;

    private String companyName;

    private String commercialLicense;

    private String licenseType;
}
