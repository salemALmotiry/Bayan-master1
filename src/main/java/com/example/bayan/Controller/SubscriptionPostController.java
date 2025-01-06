package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.Post.SubscriptionPostDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.SubscriptionPost;
import com.example.bayan.Service.SubscriptionPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/subscription-post")
@RequiredArgsConstructor
public class SubscriptionPostController {

    private final SubscriptionPostService subscriptionPostService;

    // Add Subscription Post
    @PostMapping("/create-subscription-post")
    public ResponseEntity<?> createSubscriptionPost(@AuthenticationPrincipal MyUser customer, @RequestBody @Valid SubscriptionPostDTO subscriptionPost) {
        subscriptionPostService.createSubscriptionPost(customer.getId(), subscriptionPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Post created Successfully"));
    }

    // Get myPosts
    @GetMapping("/my-posts")
    public ResponseEntity<?> mySubscriptionsPosts(@AuthenticationPrincipal MyUser customer) {
        return ResponseEntity.status(200).body(subscriptionPostService.mySubscriptionsPosts(customer.getId()));
    }

    // Update Subscription Post
    @PutMapping("/update-subscription-post/{post_id}")
    public ResponseEntity<?> updateSubscriptionPost(@AuthenticationPrincipal MyUser customer, @PathVariable Integer post_id, @RequestBody @Valid SubscriptionPostDTO subscriptionPost) {
        subscriptionPostService.updateSubscriptionPost(customer.getId(), post_id, subscriptionPost);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription Post updated Successfully"));
    }

    // Delete Subscription Post
    @DeleteMapping("/delete-subscription-post/{postId}")
    public ResponseEntity<?> deleteSubscriptionPost(@AuthenticationPrincipal MyUser customer, @PathVariable Integer postId) {
        subscriptionPostService.deleteSubscriptionPost(postId, customer.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Subscription Post deleted Successfully"));
    }


    @GetMapping("/get-all-subscription-posts")
    public ResponseEntity<?> getAllPosts(){
        return ResponseEntity.status(200).body(subscriptionPostService.getAllSubscriptionPosts());
    }

    // Create Subscription Post For Broker
    @PostMapping("/create-subscription-post-for-broker/{brokerId}")
    public ResponseEntity<?> createSubscriptionPostForBroker(@AuthenticationPrincipal MyUser customer, @PathVariable Integer brokerId, @RequestBody @Valid SubscriptionPostDTO subscriptionPost) {
        subscriptionPostService.createSubscriptionPostForBroker(customer.getId(), brokerId, subscriptionPost);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription Post created Successfully"));
    }

    // Get Posts For Broker
    @GetMapping("/posts-for-broker")
    public ResponseEntity<?> getPostForBroker(@AuthenticationPrincipal MyUser broker) {
        return ResponseEntity.status(200).body(subscriptionPostService.getPostsForBroker(broker.getId()));
    }


}
