package com.example.bayan.DTO.OUT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CbmResponseDTO {

    private double length;   // الطول
    private double width;    // العرض
    private double height;   // الارتفاع
    private int quantity;    // الكمية
    private double cbm;      // حجم الشحنة بالمتر المكعب
}
