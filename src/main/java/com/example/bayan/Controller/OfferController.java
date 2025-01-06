package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.DTO.IN.Offer.OfferDTO;
import com.example.bayan.DTO.IN.Offer.OfferWithDeliveryDTO;
import com.example.bayan.DTO.OUT.CustomerOfferDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.CustomerService;
import com.example.bayan.Service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/offer")
@RequiredArgsConstructor

public class OfferController {


    private final OfferService offerService;

    // Broker offer without delivery
    @PostMapping("/create-offer")
    public ResponseEntity<?> createOffer(@AuthenticationPrincipal MyUser broker, @RequestBody @Valid OfferDTO offerDTO) {
        offerService.createOffer(broker.getId(), offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Offer created successfully"));
    }

    @PutMapping("/update-offer/{offerId}")
    public ResponseEntity<?> updateOffer(@AuthenticationPrincipal MyUser broker, @PathVariable Integer offerId, @RequestBody @Valid OfferDTO offerDTO) {
        offerService.updateOffer(broker.getId(), offerId, offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Offer updated successfully"));
    }

    @DeleteMapping("/delete-offer/{offerId}")
    public ResponseEntity<?> deleteOffer(@AuthenticationPrincipal MyUser broker, @PathVariable Integer offerId) {
        offerService.deleteOffer(broker.getId(), offerId);
        return ResponseEntity.status(200).body(new ApiResponse("Offer deleted successfully"));
    }

    // Broker offer with delivery
    @PostMapping("/create-offer-with-delivery")
    public ResponseEntity<?> createOfferWithDelivery(@AuthenticationPrincipal MyUser broker, @RequestBody @Valid OfferWithDeliveryDTO offerDTO) {
        offerService.createOfferWithDelivery(broker.getId(), offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Offer with delivery created successfully"));
    }

    @PutMapping("/update-offer-with-delivery/{offerId}")
    public ResponseEntity<?> updateOfferWithDelivery(@AuthenticationPrincipal MyUser broker, @PathVariable Integer offerId, @RequestBody @Valid OfferWithDeliveryDTO offerDTO) {
        offerService.updateOfferWithDelivery(broker.getId(), offerId, offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Offer with delivery updated successfully"));
    }

    @DeleteMapping("/delete-offer-with-delivery/{offerId}")
    public ResponseEntity<?> removeOffer(@AuthenticationPrincipal MyUser broker, @PathVariable Integer offerId) {
        offerService.removeOffer(broker.getId(), offerId);
        return ResponseEntity.status(200).body(new ApiResponse("Offer removed successfully"));
    }


    //_________________________________________________
    @PutMapping("/accept-offer/{offerId}")
    public ResponseEntity<?> acceptOffer(@AuthenticationPrincipal MyUser customer, @PathVariable Integer offerId) {
        offerService.acceptOffer(customer.getId(), offerId);
        return ResponseEntity.status(200).body("Offer accepted successfully");
    }

    // Get all offers for a specific post for a customer
    @GetMapping("/all-offer-post/{postId}")
    public ResponseEntity<?> getAllOffersForOnePost(@AuthenticationPrincipal MyUser customer, @PathVariable Integer postId) {
        List<CustomerOfferDTO> offers = offerService.getAllOffersForOnePost(postId, customer.getId());
        return ResponseEntity.status(200).body(offers);
    }

    @GetMapping("/all-offer-sub-post/{postId}")
    public ResponseEntity<?> getAllOffersForOneSubPost(@AuthenticationPrincipal MyUser customer, @PathVariable Integer postId) {
        List<CustomerOfferDTO> offers = offerService.getAllOffersForOneSubPost(postId, customer.getId());
        return ResponseEntity.status(200).body(offers);
    }

    @PostMapping("/create-sub-offer")
    public ResponseEntity<?> createSubOffer(@AuthenticationPrincipal MyUser user, @RequestBody @Valid OfferDTO offerDTO) {
        offerService.createSubOffer(user.getId(), offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription offer created successfully"));
    }

    @PutMapping("/update-sub-offer/{offerId}")
    public ResponseEntity<?> updateSubOffer(@AuthenticationPrincipal MyUser user, @PathVariable Integer offerId, @RequestBody @Valid OfferDTO offerDTO) {
        offerService.updateSubOffer(user.getId(), offerId, offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription offer updated successfully"));
    }

    @PostMapping("/create-sub-offer-with-delivery")
    public ResponseEntity<?> createSubOfferWithDelivery(@AuthenticationPrincipal MyUser user, @RequestBody @Valid OfferWithDeliveryDTO offerDTO) {
        offerService.createSubOfferWithDelivery(user.getId(), offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription offer with delivery created successfully"));
    }

    @PutMapping("/update-sub-offer-with-delivery/{offerId}")
    public ResponseEntity<?> updateSubOfferWithDelivery(@AuthenticationPrincipal MyUser user, @PathVariable Integer offerId, @RequestBody @Valid OfferWithDeliveryDTO offerDTO) {
        offerService.updateSubOfferWithDelivery(user.getId(), offerId, offerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Subscription offer with delivery updated successfully"));
    }

    @PutMapping("/accept-offer-for-subscription-post/{offerId}")
    public ResponseEntity<?> acceptOfferForSubscriptionPost(@AuthenticationPrincipal MyUser user, @PathVariable Integer offerId) {
        offerService.acceptOfferForSubscriptionPost(user.getId(), offerId);
        return ResponseEntity.status(200).body(new ApiResponse("Offer accepted and subscription contract created successfully"));
    }

}
