package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.ReviewBrokerDTO;
import com.example.bayan.DTO.IN.ReviewCustomerDTO;
import com.example.bayan.DTO.OUT.ReviewDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bayan/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Broker rates Customer
    @PostMapping("/broker-rate-customer/{orderId}")
    public ResponseEntity<?> brokerRateCustomer(@AuthenticationPrincipal MyUser broker,
                                                @PathVariable Integer orderId,
                                                @RequestBody @Valid ReviewCustomerDTO reviewCustomerDTO) {
        reviewService.brokerRateCustomer(orderId, reviewCustomerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Review added successfully, Thank you"));
    }

    // Customer rates Broker
    @PostMapping("/customer-rate-broker/{orderId}")
    public ResponseEntity<?> customerRateBroker(@AuthenticationPrincipal MyUser customer,
                                                @PathVariable Integer orderId,
                                                @RequestBody @Valid ReviewBrokerDTO reviewBrokerDTO) {
        reviewService.customerReviewBroker(orderId, reviewBrokerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Review added successfully, Thank you"));
    }

    // Broker updates review of Customer
    @PutMapping("/broker-update-review-customer/{reviewId}")
    public ResponseEntity<?> updateBrokerReview(@AuthenticationPrincipal MyUser broker,
                                                @PathVariable Integer reviewId,
                                                @RequestBody @Valid ReviewCustomerDTO reviewCustomerDTO) {
        reviewService.updateBrokerRating(reviewId, reviewCustomerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully, Thank you"));
    }

    // Customer updates review of Broker
    @PutMapping("/customer-update-review-broker/{reviewId}")
    public ResponseEntity<?> updateCustomerReview(@AuthenticationPrincipal MyUser customer,
                                                  @PathVariable Integer reviewId,
                                                  @RequestBody @Valid ReviewBrokerDTO reviewBrokerDTO) {
        reviewService.updateCustomerReview(reviewId, reviewBrokerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully, Thank you"));
    }

    @GetMapping("/broker-reviews/{brokerId}")
    public ResponseEntity getAllReviewsOnCustomBroker(@PathVariable Integer brokerId) {

        List<ReviewDTO> reviews = reviewService.allReviewsOnCustomBroker(brokerId);

        return ResponseEntity.status(200).body(reviews);
    }


    @GetMapping("/customer/{customerId}/reviews")
    public ResponseEntity<?> getAllReviewsOnCustomer(@PathVariable Integer customerId) {
        Map<String, Object>  reviews = reviewService.allReviewsOnCustomer(customerId);
        return ResponseEntity.status(200).body(reviews);
    }

    @GetMapping("/customer/{customerId}/average-rating")
    public ResponseEntity<Map<String, Object>> getAverageRatingOnCustomer(@PathVariable Integer customerId) {
        Map<String, Object> averageRating = reviewService.allAverageOnCustomer(customerId);
        return ResponseEntity.status(200).body(averageRating);
    }

}
