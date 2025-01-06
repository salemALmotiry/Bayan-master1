package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.DeliveryDTO;
import com.example.bayan.Model.Delivery;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bayan/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // Add a new carrier for a delivery
    @PostMapping("/add-carrier/{orderId}")
    public ResponseEntity<?> addCarrier(@AuthenticationPrincipal MyUser broker, @PathVariable Integer orderId, @RequestBody @Valid DeliveryDTO delivery) {
        deliveryService.addCarrier(broker.getId(), orderId, delivery);
        return ResponseEntity.status(200).body(new ApiResponse("Carrier added successfully"));
    }

    // Update delivery status
    @PutMapping("/update-status/{deliveryId}")
    public ResponseEntity<?> updateDeliveryStatus(@AuthenticationPrincipal MyUser broker,
                                                  @PathVariable Integer deliveryId) {
        deliveryService.updateStatus(deliveryId, broker.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Delivery status updated successfully"));
    }

    @PostMapping("/track-air-shipment")
    public ResponseEntity trackAirShipment(@RequestBody Map<String, String> request) {
        String awbNumber = request.get("awb_number");

        if (awbNumber == null || awbNumber.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("AWB number is required"));
        }

        Map<String, Object> trackingDetails = deliveryService.trackAirShipment(awbNumber);

        return ResponseEntity.status(200).body(trackingDetails);
    }

    @PostMapping("/track-sea-container")
    public ResponseEntity trackSeaContainer(@RequestBody Map<String, String> request) {
        String containerNumber = request.get("container_number");

        if (containerNumber == null || containerNumber.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("Container number is required"));
        }

        Map<String, Object> trackingDetails = deliveryService.trackSeaContainer(containerNumber);

        return ResponseEntity.status(200).body(trackingDetails);
    }


    @PostMapping("/track-by-carrier/{deliveryId}/{orderId}")
    public ResponseEntity trackByCarrier(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer deliveryId, @PathVariable Integer orderId) {

        Map<String, Object> trackingDetails = deliveryService.trackByCarrier(myUser.getId(), deliveryId, orderId);

        return ResponseEntity.status(200).body(trackingDetails);
    }





}
