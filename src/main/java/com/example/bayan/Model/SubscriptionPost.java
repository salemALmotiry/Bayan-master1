package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String productCategory;

    // نوع الشحنة: صادر أو وارد
    @Column(nullable = false, length = 50)
    private String shipmentType;

    @Column(nullable = false, length = 100)
    private String countryOfOrigin;

    @Column(columnDefinition = "double not null")
    private Double weight;

    private Boolean hasDocuments;

    private Boolean hasDelivery;

    @Column(columnDefinition = "int not null")
    private Integer shipmentsNumber;

    private String status="Pending";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // The user who created the Subscription Post (customer)
    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    private CustomsBroker customsBroker;

    // Optional border reference
    //   @JoinColumn(name = "border_id")

    @ManyToOne
    @JsonIgnore
    private Border border;

    @OneToMany(mappedBy = "subscriptionPost",cascade = CascadeType.ALL)
    private Set<Offer> offers;

    @OneToMany(mappedBy ="subscriptionPost",cascade = CascadeType.ALL)
    private Set<RequiredDocuments> requiredDocsLists;

}
