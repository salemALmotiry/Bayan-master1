package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.ReviewBrokerDTO;
import com.example.bayan.DTO.IN.ReviewCustomerDTO;

import com.example.bayan.DTO.OUT.ReviewDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrdersRepository ordersRepository;
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final CustomBrokerRepository brokerRepository;
    private final AuthRepository authRepository;


    // Broker rates customer
    public void brokerRateCustomer(Integer orderId, ReviewCustomerDTO reviewDTO) {


        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new ApiException("Order not found");
        }

        if (!"Completed".equals(order.getStatus())) {
            throw new ApiException("Order is not completed. Rating is not allowed.");
        }

        CustomsBroker broker = order.getOffer().getBroker();
        if (!broker.getId().equals(reviewDTO.getReviewerBrokerId())) {
            throw new ApiException("Unauthorized broker for this order.");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setRating(reviewDTO.getRating());
        review.setReviewerBroker(broker);
        review.setReviewedCustomer(order.getOffer().getPost().getCustomer());

        reviewRepository.save(review);
    }

    // Customer review broker
    public void customerReviewBroker(Integer orderId, ReviewBrokerDTO reviewDTO) {
        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new ApiException("Order not found");
        }

        if (!"Completed".equals(order.getStatus())) {
            throw new ApiException("Order is not completed. Rating is not allowed.");
        }

        Customer customer = order.getOffer().getPost().getCustomer();
        if (!customer.getId().equals(reviewDTO.getReviewerCustomerId())) {
            throw new ApiException("Unauthorized customer for this order.");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setReviewerCustomer(customer);
        review.setReviewedBroker(order.getOffer().getBroker());

        reviewRepository.save(review);
    }


    // Update broker review for customer
    public void updateBrokerRating(Integer reviewId, ReviewCustomerDTO reviewDTO) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        if (review.getReviewerBroker() == null) {
            throw new ApiException("This review is not created by a broker.");
        }

        review.setRating(reviewDTO.getRating());

        reviewRepository.save(review);
    }

    // Update customer review for broker
    public void updateCustomerReview(Integer reviewId, ReviewBrokerDTO reviewDTO) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        if (review.getReviewerCustomer() == null) {
            throw new ApiException("This review is not created by a customer.");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
    }


    // Get all reviews for a customs broker
    public List<ReviewDTO> allReviewsOnCustomBroker(Integer brokerId){
        MyUser broker = authRepository.findMyUserById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        List<Review> reviews = reviewRepository.findReviewByReviewedBroker(broker.getBroker());
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this broker.");
        }
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review review : reviews){
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setReviewerName(review.getReviewerCustomer().getUser().getFullName());
            reviewDTO.setRating(review.getRating());
            reviewDTO.setComment(review.getComment());
            reviewDTOS.add(reviewDTO);
        }
        return reviewDTOS;
    }

    public Map<String, Object> allReviewsOnCustomer(Integer customerId) {
        // Validate the customer
        MyUser customer = authRepository.findMyUserById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }

        // Fetch reviews
        List<Review> reviews = reviewRepository.findReviewByReviewedCustomer(customer.getCustomer());
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this customer.");
        }

        // Initialize the list of review details
        List<Map<String, Object>> reviewList = new ArrayList<>();

        // Iterate through reviews
        for (Review review : reviews) {
            Map<String, Object> reviewDetails = new HashMap<>();
            reviewDetails.put("reviewerName", review.getReviewerBroker().getUser().getFullName());
            reviewDetails.put("rating", review.getRating());
            reviewList.add(reviewDetails);
        }

        // Build the result map
        Map<String, Object> result = new HashMap<>();
        result.put("reviews", reviewList);

        return result;
    }


    public Map<String, Object> allAverageOnCustomer(Integer customerId) {

        MyUser customer = authRepository.findMyUserById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }

        List<Review> reviews = reviewRepository.findReviewByReviewedCustomer(customer.getCustomer());
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this customer.");
        }

        double totalRatings = 0;

        for (Review review : reviews) {

            totalRatings += review.getRating();
        }

        // حساب متوسط التقييمات
        double averageRating = totalRatings / reviews.size();

        // إنشاء النتيجة النهائية
        Map<String, Object> result = new HashMap<>();
        result.put("averageRating", averageRating);

        return result;
    }



}

