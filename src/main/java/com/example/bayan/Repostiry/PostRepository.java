package com.example.bayan.Repostiry;

import com.example.bayan.Model.Border;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {


    List<Post> findAllByCustomerId(Integer userId);

    Post findPostById(Integer postId);

    Post findPostByIdAndCustomerId(Integer postId, Integer customerId);

//    Post findPostsByIdAnd(Integer postId, Integer customerId);


    List<Post> findPostsByCustomsBrokers(CustomsBroker broker);



    List<Post> findPostsByStatus(String status);


    List<Post>findPostByCategory(String category);

    List<Post> findPostByCountryOfOrigin(String countryOfOrigin);

    List<Post>findPostByCategoryAndCountryOfOrigin(String category, String countryOfOrigin);

    List<Post>findPostByShipmentType(String shipmentType);


    List<Post>findPostByCategoryAndShipmentType(String category, String shipmentType);


    List<Post>findPostByCategoryAndShipmentTypeAndCountryOfOrigin(String category, String shipmentType, String countryOfOrigin);



    List<Post>findAllByCustomer_Id(Integer customerId);

    List<Post> findByCustomerAndBorderAndTitleAndShipmentTypeAndCountryOfOriginAndWeight(Customer customer, Border border, String title, String shipmentType, String countryOfOrigin, Double weight);
}

