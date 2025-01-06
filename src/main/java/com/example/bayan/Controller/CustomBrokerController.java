package com.example.bayan.Controller;


import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.UpdateCustomsBrokerDTO;
import com.example.bayan.DTO.OUT.CustomBrokerFilterDTO;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Notification;
import com.example.bayan.Service.CustomBrokerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bayan/custom-broker")
@RequiredArgsConstructor
public class CustomBrokerController {

    private final CustomBrokerService brokerService;

    // register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomsBrokerDTO customsBrokerDTO){
        brokerService.register(customsBrokerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Registered Successfully"));
    }

    @PutMapping("/update-borders")
    public ResponseEntity<String> updateBrokerBorders(@AuthenticationPrincipal MyUser broker, @RequestBody List<Integer> borderIds) {
        brokerService.updateBrokerBorders(broker.getId(), borderIds);
        return ResponseEntity.ok("Borders updated successfully.");
    }

    @DeleteMapping("/remove-border/{borderId}")
    public ResponseEntity<String> removeBrokerBorder(@AuthenticationPrincipal MyUser broker, @PathVariable Integer borderId) {
        brokerService.removeBrokerBorder(broker.getId(), borderId);
        return ResponseEntity.ok("Border removed successfully.");
    }


    // get .. myProfile
    @GetMapping("/my-profile")
    public ResponseEntity<?> myProfile(@AuthenticationPrincipal MyUser broker) {
        return ResponseEntity.status(200).body(brokerService.myProfile(broker.getId()));
    }

    // update
    @PutMapping("/update-my-account")
    public ResponseEntity<?> updateMyAccount(@AuthenticationPrincipal MyUser broker,
                                             @RequestBody @Valid UpdateCustomsBrokerDTO customsBrokerDTO) {
        brokerService.updateMyAccount(broker.getId(), customsBrokerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Account updated Successfully"));
    }

    // delete
    @DeleteMapping("/delete-my-account")
    public ResponseEntity<?> deleteMyAccount(@AuthenticationPrincipal MyUser broker) {
        brokerService.deleteMyAccount(broker.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Account deleted Successfully"));
    }

    // get all custom brokers
    @GetMapping("/display-all-custom-brokers")
    public ResponseEntity<?> getAllCustomsBrokers() {
        return ResponseEntity.status(200).body(brokerService.getAllCustomsBrokers());
    }


    // Get Customs Broker by License Number
    @GetMapping("/license-number/{licenseNumber}")
    public ResponseEntity<?> getByLicenseNumber(@PathVariable String licenseNumber) {
        CustomBrokerFilterDTO customsBroker = brokerService.getByLicenseNumber(licenseNumber);
        return ResponseEntity.status(200).body(customsBroker);
    }

    // Get all Customs Brokers working at a specific border
    @GetMapping("/border/{border}")
    public ResponseEntity<?> getAllCustomsByBorder(@PathVariable String border) {
        List<CustomBrokerFilterDTO> customsBrokers = brokerService.getAllCustomsByBorder(border);
        return ResponseEntity.status(200).body(customsBrokers);
    }

    // Get all Customs Brokers by name
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getAllCustomsByName(@PathVariable String name) {
        List<CustomBrokerFilterDTO> customsBrokers = brokerService.getAllCustomsByName(name);
        return ResponseEntity.status(200).body(customsBrokers);
    }

    // Get all Customs Brokers by License Type
    @GetMapping("/license-type/{type}")
    public ResponseEntity<?> getAllCustomsByLicenseType(@PathVariable String type) {
        List<CustomBrokerFilterDTO> customsBrokers = brokerService.getAllCustomsByLicenseType(type);
        return ResponseEntity.status(200).body(customsBrokers);
    }


    @GetMapping("/offer-statistics")
    public ResponseEntity<?> getOfferStatisticsForBroker(@AuthenticationPrincipal MyUser broker) {
        Map<String, Object> stats = brokerService.getOfferStatisticsForBroker(broker.getId());
        return ResponseEntity.status(200).body(stats);
    }

    @GetMapping("/review-statistics")
    public ResponseEntity<?> getReviewStatisticsForBroker(@AuthenticationPrincipal MyUser broker) {
        Map<String, Object> stats = brokerService.getReviewStatisticsForBroker(broker.getId());
        return ResponseEntity.status(200).body(stats);
    }

    // Get all notifications for a Broker
    @GetMapping("/get-all-my-notifications")
    public ResponseEntity<?> getAllNotifications(@AuthenticationPrincipal MyUser customer) {
        List<Notification> notifications =brokerService.getAllMyNotification(customer.getId());
        return ResponseEntity.status(200).body(notifications);
    }

    // Mark a notification as read
    @PutMapping("/read-my-notifications/{notificationId}/mark-as-read")
    public ResponseEntity<?> markNotificationAsRead(@AuthenticationPrincipal MyUser customer,
                                                    @PathVariable Integer notificationId) {
        brokerService.markNotification(customer.getId(), notificationId);
        return ResponseEntity.status(200).body(new ApiResponse("Notification marked as read successfully"));
    }

}
