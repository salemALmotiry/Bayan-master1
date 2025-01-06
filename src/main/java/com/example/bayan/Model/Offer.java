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
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal deliveryPrice;


    private Boolean deliveryIncluded;

    @Column(length = 50)
    private String offerStatus = "Pending";


    // Which post does this offer belong to?
    @ManyToOne
    @JsonIgnore
    private Post post;

    // Which user (broker) made this offer?
    @ManyToOne
    @JsonIgnore
    private CustomsBroker broker;

    // If we want a relationship with Orders:
    // One offer might have one order if accepted
    @OneToOne
    @JsonIgnore
    private Orders order;

    @ManyToOne
    @JsonIgnore
    private SubscriptionPost subscriptionPost;


}
