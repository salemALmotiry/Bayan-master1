package com.example.bayan.DTO.OUT.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private String title;

    private String category;

    private Double weight;

    private String shipmentType;

    private String countryOfOrigin;


    private String status;

    private Boolean hasDelivery;

    private Boolean hasDocuments;

    private String borderName;
}

