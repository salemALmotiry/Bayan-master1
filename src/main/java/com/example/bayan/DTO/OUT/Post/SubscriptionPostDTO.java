package com.example.bayan.DTO.OUT.Post;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPostDTO {

    private String title;

    private String shipmentType;

    private Integer shipmentsNumber;

    private String productCategory;

    private String countryOfOrigin;

    private String borderName;

    private Double weight;


    private Boolean hasDocuments;

    private Boolean hasDelivery ;

    private String status;

}
