package com.example.bayan.DTO.IN.Post;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostDTO {

    @NotEmpty(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @NotEmpty(message = "Shipment type is required")
    @Size(max = 50, message = "Shipment type must not exceed 50 characters")
    private String shipmentType;

    @NotEmpty(message = "Bill of lading of origin is required")
    @Size(max = 11, message = "bill of lading must not exceed 11 characters")
    private String billOfLading;

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
