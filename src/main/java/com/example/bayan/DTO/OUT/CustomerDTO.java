package com.example.bayan.DTO.OUT;

import com.example.bayan.DTO.OUT.Post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String companyName;



}
