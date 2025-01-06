package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.Model.Address;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bayan/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // add address
    @PostMapping("/add-address")
    public ResponseEntity<?> addAddress(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid Address address) {
        addressService.addDeliveryAddress(myUser.getId(), address);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Address added Successfully"));
    }

    // get myAddresses
    @GetMapping("/my-addresses")
    public  ResponseEntity<?> myAddress(@AuthenticationPrincipal MyUser myUser){
      return ResponseEntity.status(200).body(addressService.myAddresses(myUser.getId()));
    }

    // update
    @PutMapping("/update-address/address-id/{address_id}")
    public ResponseEntity<?> updateAddress(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer address_id,@RequestBody @Valid Address address){
       addressService.updateAddress(address_id,myUser.getId(),address);
       return ResponseEntity.status(200).body(new ApiResponse("Address updated successfully"));
    }

    // delete
    @DeleteMapping("/delete-address/address-id/{address_id}")
    public ResponseEntity<?> deleteAddress(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer address_id){
        addressService.deleteAddress(myUser.getId(),address_id);
        return ResponseEntity.status(200).body(new ApiResponse("Address deleted Successfully"));
    }

}
