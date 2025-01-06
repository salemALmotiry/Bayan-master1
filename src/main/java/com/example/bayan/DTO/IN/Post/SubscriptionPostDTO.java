package com.example.bayan.DTO.IN.Post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPostDTO {

    @NotEmpty(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Shipment type is required")
    @Size(max = 50, message = "Shipment type must not exceed 50 characters")
    private String shipmentType;

    @NotNull(message = "Shipments number is required")
    @Positive(message = "shipments Number must be more than zero")
    private Integer shipmentsNumber;

    // here
    @NotEmpty(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String productCategory;

    @NotEmpty(message = "Country of origin is required")
    @Size(max = 100, message = "Country of origin must not exceed 100 characters")
    private String countryOfOrigin;

    @NotEmpty(message = "Border is required")
    @Size(max = 100, message = "Border must not exceed 30 characters")
    private String borderName;

    @NotNull(message = "Weight must not be null")
    private Double weight;

    private Boolean hasDocuments = true;
    private Boolean hasDelivery = false;

}

