package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {

    @Id
    private Integer id;

    private String companyName;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "customer")
    @MapsId
    @JsonIgnore
    private MyUser user;


    // customer relations

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private Set<Post> posts;

    @OneToMany(mappedBy ="customer",cascade =CascadeType.ALL)
    private Set<Address> addresses;

    // Customer can give and receive reviews
    @OneToMany(mappedBy = "reviewedCustomer", cascade = CascadeType.ALL)
    private Set<com.example.bayan.Model.Review> receivedReviews;

    @OneToMany(mappedBy = "reviewerCustomer", cascade = CascadeType.ALL)
    private Set<com.example.bayan.Model.Review> givenReviews;


    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private Set<Rental>rentals;

}
