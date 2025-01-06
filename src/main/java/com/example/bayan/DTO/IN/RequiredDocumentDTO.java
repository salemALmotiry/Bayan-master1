package com.example.bayan.DTO.IN;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RequiredDocumentDTO {

        // Fields for specific documents
        @NotEmpty(message = "Commercial Invoice is required.")
        @Size(max = 255, message = "Commercial Invoice URL must not exceed 255 characters.")
        private String commercialInvoice;     // الفاتورة

        @NotEmpty(message = "packingList is required.")
        @Size(max = 255, message = "packingList URL must not exceed 255 characters.")
        private String packingList;           // قائمة التعبئة

        @NotEmpty(message = "Bill of lading of origin is required")
        @Size(max = 11, message = "bill of lading must not exceed 11 characters")
        private String billOfLading;          // بوليصة الشحن

        @NotEmpty(message = "Certificate of Origin is required.")
        @Size(max = 255, message = "Certificate of Origin URL must not exceed 255 characters.")
        private String certificateOfOrigin;   // شهادة المنشأ

        @Size(max = 255, message = "Document URL must not exceed 255 characters.")
        private String otherDocument;    // مستندات اضافية

}
