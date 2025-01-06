package com.example.bayan.Repostiry;

import com.example.bayan.Model.Border;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.Post;
import com.example.bayan.Model.SubscriptionPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPostRepository extends JpaRepository<SubscriptionPost,Integer> {

    List<SubscriptionPost> findAllByCustomerId(Integer userId);

    SubscriptionPost findSubscriptionPostById(Integer postId);


    SubscriptionPost findPostByIdAndCustomerId(Integer postId,Integer customerId);

    List<SubscriptionPost> findSubscriptionPostByCustomsBrokerId(Integer customerId);
    List<SubscriptionPost> findPostsByStatus(String status);

    List<SubscriptionPost> findByCustomerAndBorderAndTitleAndShipmentTypeAndCountryOfOriginAndWeightAndShipmentsNumber(Customer customer, Border border, String title, String shipmentType, String countryOfOrigin, Double weight, Integer shipmentsNumber);
}
