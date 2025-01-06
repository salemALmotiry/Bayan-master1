package com.example.bayan.Controller;


import com.example.bayan.DTO.OUT.BrokerRentalsDTO;
import com.example.bayan.DTO.OUT.CustomerRentalsDTO;
import com.example.bayan.DTO.OUT.RentalDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping("/customer-rentals")
    public ResponseEntity<?> getCustomerRentals(@AuthenticationPrincipal MyUser customer) {
        List<RentalDTO> rentals = rentalService.getCustomerRental(customer.getId());
        return ResponseEntity.status(200).body(rentals);
    }

    @GetMapping("/customer-with-rentals")
    public ResponseEntity<?> getCustomerWithRentals(@AuthenticationPrincipal MyUser customer) {
        CustomerRentalsDTO customerRentals = rentalService.getCustomerWithRentals(customer.getId());
        return ResponseEntity.status(200).body(customerRentals);
    }

    @GetMapping("/broker-rentals")
    public ResponseEntity<?> getBrokerRentals(@AuthenticationPrincipal MyUser broker) {
        List<RentalDTO> rentals = rentalService.getBrokerRental(broker.getId());
        return ResponseEntity.status(200).body(rentals);
    }

    @GetMapping("/broker-with-rentals")
    public ResponseEntity<?> getBrokerWithRentals(@AuthenticationPrincipal MyUser broker) {
        BrokerRentalsDTO brokerRentals = rentalService.getBrokerWithRentals(broker.getId());
        return ResponseEntity.status(200).body(brokerRentals);
    }


}
