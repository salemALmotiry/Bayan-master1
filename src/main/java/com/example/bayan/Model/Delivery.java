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
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Column(length = 100)
    private String carrier;

    @Column(length = 100)
    private String trackingNumber;

    @Column(length = 50)
    private String status = "PENDING";


    @ManyToOne
    @JsonIgnore
    private Orders order;


}
