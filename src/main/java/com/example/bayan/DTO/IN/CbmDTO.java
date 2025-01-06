package com.example.bayan.DTO.IN;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CbmDTO {

    @Min(value = 0, message = "Length must be greater than or equal to 0")
    private double length;

    @Min(value = 0, message = "Width must be greater than or equal to 0")
    private double width;

    @Min(value = 0, message = "Height must be greater than or equal to 0")
    private double height;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Unit cannot be null")
    @Pattern(regexp = "^(cm|in)$", message = "Unit must be either 'cm' or 'in'")
    private String unit;

}
