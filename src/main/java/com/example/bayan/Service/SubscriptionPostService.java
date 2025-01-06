package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.SubscriptionPostDTO;

import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPostService {

    private final SubscriptionPostRepository subscriptionPostRepository;
    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final BorderRepository borderRepository;
    private final CustomBrokerRepository customBrokerRepository;


    // create a post for Subscription shipments
    // customer create Subscription Post to request for many shipments
    public void createSubscriptionPost(Integer customerId, SubscriptionPostDTO subscriptionPostDTO) {
        MyUser user = authRepository.findMyUserById(customerId);
        if (user == null) throw new ApiException("Invalid user ID.");

        Customer customer = customerRepository.findCustomerByUser(user);

        if (customer == null)
            throw new ApiException("Customer not found for the user.");

        Border border = borderRepository.findBorderByName(subscriptionPostDTO.getBorderName());

        if (border == null)
            throw new ApiException("No border found with the name: " + subscriptionPostDTO.getBorderName());

        // to check if there is an existing post with same details in pending status
        List<SubscriptionPost> existingPosts = subscriptionPostRepository.findByCustomerAndBorderAndTitleAndShipmentTypeAndCountryOfOriginAndWeightAndShipmentsNumber(
                customer, border, subscriptionPostDTO.getTitle(), subscriptionPostDTO.getShipmentType(),
                subscriptionPostDTO.getCountryOfOrigin(), subscriptionPostDTO.getWeight(), subscriptionPostDTO.getShipmentsNumber());

        if (!existingPosts.isEmpty()) {
            for (SubscriptionPost existingPost : existingPosts) {
                if ("Pending".equals(existingPost.getStatus())) {
                    throw new ApiException("A similar post already exists with Pending status.");
                }
            }
        }
        SubscriptionPost post = new SubscriptionPost();
        post.setProductCategory(subscriptionPostDTO.getProductCategory());
        post.setTitle(subscriptionPostDTO.getTitle());
        post.setShipmentType(subscriptionPostDTO.getShipmentType());
        post.setCountryOfOrigin(subscriptionPostDTO.getCountryOfOrigin());
        post.setWeight(subscriptionPostDTO.getWeight());
        post.setHasDocuments(subscriptionPostDTO.getHasDocuments());
        post.setHasDelivery(subscriptionPostDTO.getHasDelivery());
        post.setShipmentsNumber(subscriptionPostDTO.getShipmentsNumber());
        post.setCustomer(customer);
        post.setBorder(border);

        subscriptionPostRepository.save(post);
    }

    public void createSubscriptionPostForBroker(Integer customerId, Integer brokerId, SubscriptionPostDTO subscriptionPostDTO) {
        // Validate the customer
        MyUser customer = authRepository.findMyUserById(customerId);
        if (customer == null)
            throw new ApiException("Customer with ID " + customerId + " does not exist.");


        Customer customerEntity = customerRepository.findCustomerByUser(customer);
        if (customerEntity == null)
            throw new ApiException("Customer entity not found for the user.");


        // Validate the broker
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        // Validate the border
        Border border = borderRepository.findBorderByName(subscriptionPostDTO.getBorderName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + subscriptionPostDTO.getBorderName());
        }


        // Create a new SubscriptionPost entity
        SubscriptionPost post = new SubscriptionPost();

        post.setTitle(subscriptionPostDTO.getTitle());
        post.setShipmentType(subscriptionPostDTO.getShipmentType());
        post.setCountryOfOrigin(subscriptionPostDTO.getCountryOfOrigin());
        post.setWeight(subscriptionPostDTO.getWeight());
        post.setProductCategory(subscriptionPostDTO.getProductCategory());
        post.setHasDocuments(subscriptionPostDTO.getHasDocuments());
        post.setHasDelivery(subscriptionPostDTO.getHasDelivery());
        post.setShipmentsNumber(subscriptionPostDTO.getShipmentsNumber());
        post.setCustomer(customerEntity);
        post.setBorder(border);
        post.setCustomsBroker(broker); // ربط الـ SubscriptionPost بالبروكر


        // Save the new subscription post
        subscriptionPostRepository.save(post);


    }
/////////////////


    public List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> getPostsForBroker(Integer brokerId) {
        // Validate the broker

        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if (broker == null)
            throw new ApiException("Broker with ID " + brokerId + " not found.");


        // Fetch all posts associated with the broker
        List<SubscriptionPost> posts = subscriptionPostRepository.findSubscriptionPostByCustomsBrokerId(brokerId);

        if (posts.isEmpty())
            throw new ApiException("No posts found for the broker with ID " + brokerId);


        // Map Post entities to PostDTO objects
        List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> postDTOs = new ArrayList<>();

        for (SubscriptionPost post : posts) {

            com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO dto = new com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO();

            dto.setTitle(post.getTitle());
            dto.setProductCategory(post.getProductCategory());

            dto.setWeight(post.getWeight());

            dto.setShipmentType(post.getShipmentType());

            dto.setCountryOfOrigin(post.getCountryOfOrigin());

            dto.setStatus(post.getStatus());

            dto.setHasDelivery(post.getHasDelivery());

            dto.setHasDocuments(post.getHasDocuments());
            postDTOs.add(dto);
        }

        return postDTOs;
    }


    // get myPost .. subscription Posts by customer
    public List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> mySubscriptionsPosts(Integer customer_id){
        MyUser user = authRepository.findMyUserById(customer_id);
        if (user == null) {
            throw new ApiException("Wrong user Id");
        }
        Customer customer = customerRepository.findCustomerByUser(user);
        if (customer == null) {
            throw new ApiException("Customer not found for the user.");
        }

        List<SubscriptionPost> posts = subscriptionPostRepository.findAllByCustomerId(customer_id);

        List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> subscriptionPostDTOS = new ArrayList<>();

        for (SubscriptionPost post : posts){

            com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO postDTO = new com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO();

            postDTO.setTitle(post.getTitle());
            postDTO.setProductCategory(post.getProductCategory());
            postDTO.setWeight(post.getWeight());
            postDTO.setShipmentType(post.getShipmentType());
            postDTO.setCountryOfOrigin(post.getCountryOfOrigin());
            postDTO.setStatus(post.getStatus());
            postDTO.setHasDelivery(post.getHasDelivery());
            postDTO.setHasDocuments(post.getHasDocuments());
            postDTO.setShipmentsNumber(post.getShipmentsNumber());

            subscriptionPostDTOS.add(postDTO);
        }
        return subscriptionPostDTOS;
    }

    // update subscription Post
    public void updateSubscriptionPost(Integer customerId, Integer postId, SubscriptionPostDTO subscriptionPostDTO) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found.");

        SubscriptionPost post = subscriptionPostRepository.findPostByIdAndCustomerId(postId,customerId);
        if (post == null) throw new ApiException("Post not found.");
        if (!post.getCustomer().getId().equals(customerId)) throw new ApiException("Unauthorized access.");
        if (!"Pending".equals(post.getStatus())) throw new ApiException("Only 'Pending' posts can be updated.");

        Border border = borderRepository.findBorderByName(subscriptionPostDTO.getBorderName());
        if (border == null) throw new ApiException("Border not found: " + subscriptionPostDTO.getBorderName());

        post.setTitle(subscriptionPostDTO.getTitle());
        post.setShipmentType(subscriptionPostDTO.getShipmentType());
        post.setCountryOfOrigin(subscriptionPostDTO.getCountryOfOrigin());
        post.setWeight(subscriptionPostDTO.getWeight());
        post.setHasDocuments(subscriptionPostDTO.getHasDocuments());
        post.setHasDelivery(subscriptionPostDTO.getHasDelivery());
        post.setShipmentsNumber(subscriptionPostDTO.getShipmentsNumber());
        post.setBorder(border);

        subscriptionPostRepository.save(post);
    }

    // delete subscription Post
    public void deleteSubscriptionPost(Integer postId, Integer customerId) {
        SubscriptionPost post = subscriptionPostRepository.findPostByIdAndCustomerId(postId,customerId);
        if (post == null) throw new ApiException("Post not found.");
        if (!"Pending".equals(post.getStatus())) throw new ApiException("Only 'Pending' posts can be deleted.");

        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null || !post.getCustomer().getId().equals(customerId))
            throw new ApiException("Unauthorized access.");

        subscriptionPostRepository.delete(post);
    }

    // get all Subscriptions Posts

    public List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> getAllSubscriptionPosts(){
        List<SubscriptionPost> posts = subscriptionPostRepository.findPostsByStatus("Pending");

        if (posts.isEmpty()) {
            throw new ApiException("No posts found");
        }
        List<com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO> postDTOS = new ArrayList<>();
        for (SubscriptionPost post : posts) {
            com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO dto = new com.example.bayan.DTO.OUT.Post.SubscriptionPostDTO();
            dto.setTitle(post.getTitle());
            dto.setProductCategory(post.getProductCategory());
            dto.setWeight(post.getWeight());
            dto.setShipmentType(post.getShipmentType());
            dto.setCountryOfOrigin(post.getCountryOfOrigin());
            dto.setWeight(post.getWeight());
            dto.setHasDelivery(post.getHasDelivery());
            dto.setHasDocuments(post.getHasDocuments());
            dto.setShipmentsNumber(post.getShipmentsNumber());
            postDTOS.add(dto);
        }
        return postDTOS;
    }

}
