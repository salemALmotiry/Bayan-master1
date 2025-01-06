package com.example.bayan.DTO.IN;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    @NotEmpty(message = "Customer Username cannot be empty")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters")
    private String username;

    @NotEmpty(message = "Customer Uname cannot be empty")
    @Size(min = 4, max = 20, message = "Name must be between 4 and 20 characters")
    private String fullName;

    @NotEmpty(message = "Customer Password cannot be empty")
    @Size(min = 6, max = 300, message = "User Password must be at least 6 characters")
    private String password;


    @NotEmpty(message = "Customer Email cannot be empty")
    @Email(message = "Customer Email must be a valid email format")
    private String email;



    @NotEmpty(message = "Customer Phone Number cannot be empty")
    @Pattern(regexp = "^05\\d{8}$", message = "Customer Phone Number must start with '05' and be exactly 10 digits long.")
    private String phoneNumber;


    @Size(min = 4, max = 20, message = "Company name must be between 4 and 20 characters")
    private String companyName;









}
