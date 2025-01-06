package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.RequiredDocumentDTO;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Post;
import com.example.bayan.Model.RequiredDocuments;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.OfferRepository;
import com.example.bayan.Repostiry.PostRepository;
import com.example.bayan.Repostiry.RequiredDocumentsRepository;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequiredDocumentsService {

    private final RequiredDocumentsRepository requiredDocumentsRepository;
    private final PostRepository postRepository;
    private final AuthRepository authRepository;
    private final OfferRepository offerRepository;

    public List<String> uploadFiles(List<MultipartFile> files, Integer postId, Integer userId) {
        if (files.isEmpty()) {
            throw new ApiException("Please upload files to proceed.");
        }

        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Post post = postRepository.findPostByIdAndCustomerId(postId, userId);
        if (post == null) {
            throw new ApiException("Post with ID " + postId + " not found");
        }

        // Check existing files for the post
        List<RequiredDocuments> existingDocuments = requiredDocumentsRepository.findRequiredDocumentsByPostId(postId);
        if (existingDocuments.size() >= 5) {
            throw new ApiException("Cannot add more than 5 files to this post.");
        }

        String uploadDir = "uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        List<String> uploadedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    if (existingDocuments.size() + uploadedFiles.size() >= 5) {
                        throw new ApiException("Maximum allowed files (5) reached. Cannot add more.");
                    }

                    // Check if the file is already uploaded
                    boolean isDuplicate = existingDocuments.stream()
                            .anyMatch(doc -> doc.getDocName().equals(file.getOriginalFilename()));

                    if (isDuplicate) {
                        throw new ApiException("Duplicate file detected: " + file.getOriginalFilename());
                    }

                    // Generate a unique filename using postId, userId, and original filename
                    String uniqueFileName = postId + "_" + userId + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + uniqueFileName);
                    Files.write(filePath, file.getBytes());

                    // Save metadata in the database
                    RequiredDocuments document = new RequiredDocuments();
                    document.setDocName(file.getOriginalFilename()); // Save the original file name
                    document.setDocUrl(filePath.toString());
                    document.setPost(post);
                    requiredDocumentsRepository.save(document);

                    uploadedFiles.add(uniqueFileName);
                }
            }
        } catch (IOException e) {
            throw new ApiException("An error occurred while uploading files: " + e.getMessage());
        }

        return uploadedFiles;
    }

    public Map<String, Object> getFilesByPostAndUser(Integer postId, Integer userId) {
        // Validate the user
        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        // Validate the post
        Post post = postRepository.findPostByIdAndCustomerId(postId, userId);
        if (post == null) {
            throw new ApiException("Post with ID " + postId + " not found");
        }

        // Fetch documents for the post
        List<RequiredDocuments> documents = requiredDocumentsRepository.findRequiredDocumentsByPostId(postId);
        if (documents.isEmpty()) {
            throw new ApiException("No files found for the specified post and user.");
        }

        // Extract file names into a list
        List<String> fileNames = new ArrayList<>();
        for (RequiredDocuments document : documents) {
            fileNames.add(document.getDocName()); // Assuming `getFileName` is a method in `RequiredDocuments`
        }

        // Create the result map
        Map<String, Object> result = new HashMap<>();
        result.put("fileNames", fileNames);

        return result;
    }

    public Map<String, Object> getFilesByPostAndBroker(Integer postId, Integer brokerId, Integer customerId) {
        // Validate the broker
        MyUser broker = authRepository.findMyUserById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker not found.");
        }

        // Validate the post
        Post post = postRepository.findPostByIdAndCustomerId(postId, customerId);
        if (post == null) {
            throw new ApiException("Post with ID " + postId + " not found or does not belong to the specified customer.");
        }

        // Validate the offer
        Offer offer = offerRepository.findActiveOfferByIdAndPostCustomerIdAndBrokerId(postId, customerId, brokerId);
        if (offer == null) {
            throw new ApiException("No active offer found for the specified post, customer, and broker.");
        }

        // Fetch documents for the post
        List<RequiredDocuments> documents = requiredDocumentsRepository.findRequiredDocumentsByPostId(postId);
        if (documents.isEmpty()) {
            throw new ApiException("No files found for the specified post.");
        }

        // Extract file names into a list
        List<String> fileNames = new ArrayList<>();
        for (RequiredDocuments document : documents) {
            if (!document.getPost().getId().equals(post.getId())) {
                throw new ApiException("Document does not belong to the specified post.");
            }
            fileNames.add(document.getDocName());
        }

        // Create the result map
        Map<String, Object> result = new HashMap<>();
        result.put("fileNames", fileNames);

        return result;
    }

    // download files by broker
    public Resource downloadFileForBroker(Integer documentId, Integer offerId, Integer brokerId) {

        MyUser broker = authRepository.findMyUserById(brokerId);
        if (broker == null)
            throw new ApiException("broker not found");

        Offer offer1 = offerRepository.findOfferById(offerId);
        Post post  = postRepository.findPostById(offer1.getPost().getId());
        if (post == null)
            throw new ApiException("post not found");

        Offer offer = offerRepository.findActiveOfferByIdAndPostCustomerIdAndBrokerId(
                                         documentId,post.getCustomer().getId(),brokerId);
        if (offer == null)
            throw new ApiException("offer not found");


        RequiredDocuments document = requiredDocumentsRepository.
                findRequiredDocumentsByIdAndPostId(documentId,offer.getPost().getId());
        if (document == null){
            throw new ApiException("document not found");
        }

        if (!document.getPost().getId().equals(offer.getPost().getId())) {
            throw new ApiException("Document does not belong to the specified offer.");
        }

        try {
            Path filePath = Paths.get(document.getDocUrl()).normalize();
            File file = filePath.toFile();

            if (!file.exists() || !file.canRead()) {
                throw new ApiException("File not found or not readable: " + document.getDocUrl());
            }

            return new FileSystemResource(file);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    public Resource downloadFileForCustomer(Integer documentId, Integer postId, Integer customerId) {


        MyUser customer = authRepository.findMyUserById(customerId);
        if (customer == null)
            throw new ApiException("Customer not found");

        Post post = postRepository.
                findPostByIdAndCustomerId(postId, customerId);
        if (post == null)
            throw new ApiException("Post not found or does not belong to the specified customer");

        RequiredDocuments document = requiredDocumentsRepository.
                findRequiredDocumentsByIdAndPostId(documentId, postId);
        if (document == null) {
            throw new ApiException("Document not found");
        }

        if (!document.getPost().getId().equals(post.getId())) {
            throw new ApiException("Document does not belong to the specified post.");
        }

        try {
            Path filePath = Paths.get(document.getDocUrl()).normalize();
            File file = filePath.toFile();

            if (!file.exists() || !file.canRead()) {
                throw new ApiException("File not found or not readable: " + document.getDocUrl());
            }

            return new FileSystemResource(file);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

}
