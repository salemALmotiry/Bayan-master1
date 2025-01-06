package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Orders {

    @Id
    private Integer id;

    @Column(length = 50)
    private String status = "PLACED";

    @Column(length = 50)
    private String paymentStatus = "PENDING";


    // The accepted offer that created this order
    @OneToOne
    @MapsId
    private Offer offer;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<ChatMessages> chatMessages;

    // If referencing Delivery by offer instead of post:
     @OneToMany(mappedBy="order", cascade = CascadeType.ALL)
     private Set<Delivery> deliveries;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<com.example.bayan.Model.Review> reviews;

}
