package com.example.bayan.Model;

import com.example.bayan.Model.Customer;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;

    private String comment;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relation to Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    // Distinguish the reviewer and the reviewed
    @ManyToOne
    @JoinColumn(name = "reviewer_customer_id")
    private Customer reviewerCustomer;

    @ManyToOne
    @JoinColumn(name = "reviewer_broker_id")
    private CustomsBroker reviewerBroker;

    @ManyToOne
    @JoinColumn(name = "reviewed_customer_id")
    private Customer reviewedCustomer;

    @ManyToOne
    @JoinColumn(name = "reviewed_broker_id")
    private CustomsBroker reviewedBroker;

}

