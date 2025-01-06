package com.example.bayan.DTO.IN;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomsBrokerDTO {

    // my user

    // username
    @NotEmpty(message = "username is required")
    @Size(min = 3, max = 100, message = "username must be between 3 and 100 characters")
    private String username;

    //email
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email format")
    private String email;

    // phoneNumer
    @NotEmpty(message = "PhoneNumber is Required")
    @Pattern(regexp = "^(\\+966|0)?5\\d{8}$",   message = "Phone number must start with +966 or 05 and be followed by 8 digits")
    private String phoneNumber;

    @NotEmpty(message = "fullName is Required")
    private String fullName;

    // customBroker

    @NotEmpty(message = "License number is required")
    @Pattern(regexp = "\\d+", message = "License number must contain only numbers")
    private String licenseNumber;

    @NotEmpty(message = "companyName is required")
    private String companyName;

    @NotEmpty(message = "Commercial license is required")
    @Pattern(regexp = "7\\d{9}", message = "Commercial license must be 10 digits starting with 7")
    private String commercialLicense;

    // Filter by customs broker type
    // انواع المخلصين : مخلص وارد و صادر و ترانزيت
    //مخلص صادر و وارد
    // مخلص وارد و صادر
    @NotEmpty(message = "licenseType is required")
    @Pattern(
            regexp = "Importer and Exporter|Importer, Exporter, and Transit|Exporter and Importer",
            message = "License type must be one of: 'Importer and Exporter', 'Importer, Exporter, and Transit', 'Exporter and Importer'"
    )
    private String licenseType;

}
