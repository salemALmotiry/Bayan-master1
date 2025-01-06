package com.example.bayan.DTO.OUT;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBrokerForAdminDTO {

    // username
    private String username;

    //email
    private String email;

    // phoneNubmer
    private String phoneNumber;

    private String fullName;

    private String licenseNumber;

    private String companyName;

    private String commercialLicense;

    private String licenseType;

    private Boolean isActive;
}
