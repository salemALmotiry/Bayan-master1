package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.IN.RequiredDocumentDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.RequiredDocuments;
import com.example.bayan.Repostiry.RequiredDocumentsRepository;
import com.example.bayan.Service.RequiredDocumentsService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bayan/required-documents")
@RequiredArgsConstructor
public class RequiredDocumentsController {

    private final RequiredDocumentsService requiredDocumentsService;


    @PostMapping("/upload-multiple/{postId}")
    public ResponseEntity<?> uploadMultipleFiles(@AuthenticationPrincipal MyUser user,
                                                 @RequestParam("files") List<MultipartFile> files,
                                                 @PathVariable("postId") Integer postId) {
        List<String> uploadedFiles = requiredDocumentsService.uploadFiles(files, postId, user.getId());
        return ResponseEntity.ok("Files uploaded successfully: " + uploadedFiles);
    }

    @GetMapping("/get-files/{postId}")
    public ResponseEntity<?> getFilesByPostAndUser(@AuthenticationPrincipal MyUser user,
                                                   @PathVariable("postId") Integer postId) {
        Map<String, Object>  documents = requiredDocumentsService.getFilesByPostAndUser(postId, user.getId());
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/get-files-broker/{postId}/{customerId}")
    public ResponseEntity<?> getFilesByPostAndUserForBroker(@AuthenticationPrincipal MyUser user,
                                                   @PathVariable Integer customerId,
                                                   @PathVariable("postId") Integer postId) {
        Map<String, Object>  documents = requiredDocumentsService.getFilesByPostAndBroker(postId,user.getId(),customerId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/download/{offerId}/{documentId}")
    public ResponseEntity<Resource> downloadFile(@AuthenticationPrincipal MyUser user,
                                                 @PathVariable("documentId") Integer documentId,
                                                 @PathVariable("offerId") Integer offerId) {
        Resource resource = requiredDocumentsService.downloadFileForBroker(documentId, offerId, user.getId());

        String contentType = "application/octet-stream";
        String fileName = resource.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/download-for-customer/{postId}/{documentId}")
    public ResponseEntity<Resource> downloadFileForCustomer(@AuthenticationPrincipal MyUser user,
                                                            @PathVariable("documentId") Integer documentId,
                                                            @PathVariable("postId") Integer postId) {
        Resource resource = requiredDocumentsService.downloadFileForCustomer(documentId, postId, user.getId());

        String contentType = "application/octet-stream";
        String fileName = resource.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }


}
