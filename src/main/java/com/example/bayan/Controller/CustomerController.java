package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CbmDTO;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.UpdateCustomerDTO;
import com.example.bayan.DTO.OUT.CbmResponseDTO;
import com.example.bayan.Model.Notification;
import com.example.bayan.Repostiry.NotificationRepository;
import com.example.bayan.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.bayan.Model.MyUser;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final NotificationRepository notificationRepository;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
        return ResponseEntity.ok().body(new ApiResponse("Customer registered Successfully"));
    }

    // get .. myAccount profile
    @GetMapping("/my-account")
    public ResponseEntity<?> myProfile(@AuthenticationPrincipal MyUser customer) {
        return ResponseEntity.status(200).body(customerService.getMyAccount(customer.getId()));
    }

    // update
    @PutMapping("/update-my-account")
    public ResponseEntity<?> updateMyAccount(@AuthenticationPrincipal MyUser customer,
                                             @RequestBody @Valid UpdateCustomerDTO customerDTO) {
        customerService.updateCustomerAccount(customer.getId(), customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Account updated Successfully"));
    }



    @PostMapping("/calculate-cbm")
    public ResponseEntity calculateCbm(@RequestBody @Valid CbmDTO cbmDTO) {

        CbmResponseDTO response = customerService.calculateCbm(cbmDTO);

        return ResponseEntity.status(200).body(response);
    }



    // Get all notifications for a customer
    @GetMapping("/get-all-my-notifications")
    public ResponseEntity<?> getAllNotifications(@AuthenticationPrincipal MyUser customer) {
        List<Notification> notifications = customerService.getAllMyNotification(customer.getId());
        return ResponseEntity.status(200).body(notifications);
    }

    // Mark a notification as read
    @PutMapping("/read-my-notifications/{notificationId}/mark-as-read")
    public ResponseEntity<?> markNotificationAsRead(@AuthenticationPrincipal MyUser customer,
                                                    @PathVariable Integer notificationId) {
        customerService.markNotification(notificationId, customer.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Notification marked as read successfully"));
    }








}
