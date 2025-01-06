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
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomsBroker {

    @Id
    private Integer id;

    private String licenseNumber;


    private String companyName;

    private String commercialLicense;

    private String licenseType;


     private Boolean isActive=false;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ElementCollection
    @CollectionTable(name = "custom_broker_borders", joinColumns = @JoinColumn(name = "custom_broker_id"))
    @Column(name = "border_name")
    private Set<String> borderNames = new HashSet<>();

    // One-to-one with user if role = BROKER
    @OneToOne(mappedBy = "broker")
    @MapsId
    @JsonIgnore
    private MyUser user;

    @ManyToMany
    @JoinTable(
            name = "customs_broker_borders",
            joinColumns = @JoinColumn(name = "broker_id"),
            inverseJoinColumns = @JoinColumn(name = "border_id")
    )
    private Set<Border> borders;

    @OneToMany(mappedBy = "broker",cascade = CascadeType.ALL)
    private Set<Offer> offers;


    @OneToMany
    private Set<Post> post;

    @OneToMany
    private Set<SubscriptionPost> subscriptionPosts;

    // Broker can give and receive reviews
    @OneToMany(mappedBy = "reviewedBroker", cascade = CascadeType.ALL)
    private Set<com.example.bayan.Model.Review> receivedReviews;

    @OneToMany(mappedBy = "reviewerBroker", cascade = CascadeType.ALL)
    private Set<com.example.bayan.Model.Review> givenReviews;

    @OneToMany(mappedBy = "broker",cascade = CascadeType.ALL)
    private Set<Rental>rentals;



}
