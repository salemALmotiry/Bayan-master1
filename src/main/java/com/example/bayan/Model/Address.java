package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // A user can have many addresses

    @Column(length = 100)
    private String city;

    @Column(length = 250)
    private String neighborhood;

    @Column(length = 100)
    private String street;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 20)
    private String buildingNumber;


    @ManyToOne
    @JsonIgnore
    private Customer customer;

}
