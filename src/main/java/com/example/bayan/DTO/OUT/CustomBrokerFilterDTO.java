package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBrokerFilterDTO {

    private String brokerName;

    private String licenseNumber;

    private String commercialLicense;

    private String licenseType;

}
