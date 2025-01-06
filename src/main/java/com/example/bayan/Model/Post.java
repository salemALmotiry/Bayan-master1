package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comments;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String category;

    // نوع الشحنة: صادر أو وارد
    @Column(nullable = false, length = 50)
    private String shipmentType;

    @Column(nullable = false, length = 11)

    private String billOfLading;

    @Column(nullable = false, length = 100)
    private String countryOfOrigin;


    private Double weight;

    private Boolean hasDocuments;

    private Boolean hasDelivery;


    @Column(nullable = false)
    private String status="Pending";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    // The user who created the post (customer)
    @ManyToOne
    @JsonIgnore
    private Customer customer;

    // Optional border reference
    //    @JoinColumn(name = "border_id")

    @ManyToOne
    @JsonIgnore
    private Border border;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<Offer> offers;

    @ManyToOne
    private CustomsBroker customsBrokers;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<RequiredDocuments> requiredDocsLists;


}
