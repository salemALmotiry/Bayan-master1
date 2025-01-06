package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.OUT.CustomBrokerForAdminDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bayan/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PutMapping("/accept-custom-broker/custom-broker/{customerId}")
    public ResponseEntity<?> acceptCustomBroker(@AuthenticationPrincipal MyUser admin, @PathVariable Integer customerId) {
        authService.acceptCustomBroker(admin.getId(), customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customs Broker has been activated successfully."));
    }

    @PutMapping("/reject-custom-broker/custom-broker/{customerId}")
    public ResponseEntity<?> rejectCustomBroker(@AuthenticationPrincipal MyUser admin, @PathVariable Integer customerId) {
        authService.rejectCustomBroker(admin.getId(), customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customs Broker has been rejected successfully."));
    }

    @GetMapping("/custom-brokers")
    public ResponseEntity<List<CustomBrokerForAdminDTO>> getAllCustomBrokers(@AuthenticationPrincipal MyUser admin) {
        List<CustomBrokerForAdminDTO> brokers = authService.getAllCustomBroker(admin.getId());
        return ResponseEntity.status(200).body(brokers);
    }


}
