package com.example.bayan.Repostiry;

import com.example.bayan.Model.Customer;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);

    List<Review> findByReviewedBroker(CustomsBroker reviewedBroker);
    List<Review> findReviewByReviewedBroker(CustomsBroker reviewedBroker);
    List<Review> findReviewByReviewedCustomer(Customer customer);
}
