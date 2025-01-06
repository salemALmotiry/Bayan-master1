package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Post.AddPostDTO;
import com.example.bayan.DTO.OUT.Post.PostDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    private final BorderRepository borderRepository;
    private final AuthRepository authRepository;
    private final CustomBrokerRepository customBrokerRepository;
    private final RentalRepository rentalRepository;
    private final OfferRepository offerRepository;
    private final OrdersRepository ordersRepository;
    private final EmailService emailService;


    public void addPost(Integer userId, AddPostDTO addPostDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);
        if (myUser == null) {
            throw new ApiException("User not found");
        }

        Customer customer = customerRepository.findCustomerByUser(myUser);
        if (customer == null) {
            throw new ApiException("Customer profile not found for the user.");
        }

        Border border = borderRepository.findBorderByName(addPostDTO.getBorderName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + addPostDTO.getBorderName());
        }

        // to check if there is an existing post with same details in pending status
        List<Post> existingPosts = postRepository.findByCustomerAndBorderAndTitleAndShipmentTypeAndCountryOfOriginAndWeight(
                customer, border, addPostDTO.getTitle(), addPostDTO.getShipmentType(),
                addPostDTO.getCountryOfOrigin(), addPostDTO.getWeight());

        if (!existingPosts.isEmpty()) {
            for (Post existingPost : existingPosts) {
                if ("Pending".equals(existingPost.getStatus())) {
                    throw new ApiException("A similar post already exists with Pending status.");
                }
            }
        }
        // Create a new Post entity
        Post post = new Post();
        post.setTitle(addPostDTO.getTitle());
        post.setCategory(addPostDTO.getCategory());
        post.setShipmentType(addPostDTO.getShipmentType());
        post.setCountryOfOrigin(addPostDTO.getCountryOfOrigin());
        post.setWeight(addPostDTO.getWeight());
        post.setHasDocuments(addPostDTO.getHasDocuments());
        post.setHasDelivery(addPostDTO.getHasDelivery());
        post.setBillOfLading(addPostDTO.getBillOfLading());
        post.setCustomer(customer); // Set the customer
        post.setBorder(border);     // Set the border
        postRepository.save(post);
    }

    public List<PostDTO> myPost(Integer userId) {

        MyUser myUser = authRepository.findMyUserById(userId);
        if (myUser == null) {
            throw new ApiException("User not found");
        }
        Customer customer = customerRepository.findCustomerByUser(myUser);

        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        // Fetch posts associated with the customer
        List<Post> posts = postRepository.findAllByCustomerId(customer.getId());

        // Prepare the list of PostDTO
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            dto.setTitle(post.getTitle());
            dto.setCategory(post.getCategory());
            dto.setWeight(post.getWeight());
            dto.setShipmentType(post.getShipmentType());
            dto.setCountryOfOrigin(post.getCountryOfOrigin());
            dto.setStatus(post.getStatus());
            dto.setHasDelivery(post.getHasDelivery());
            dto.setHasDocuments(post.getHasDocuments());
            dto.setBorderName(post.getBorder().getName());

            postDTOList.add(dto);
        }

        return postDTOList;
    }

    public void update(Integer userId,Integer postId,AddPostDTO addPostDTO){

        Customer customer = customerRepository.findCustomerById(userId);

        if (customer == null){
            throw new ApiException("Customer with this"+userId+" does not exist");

        }

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new ApiException("Post with this"+postId+" does not exist");

        }
        if (!post.getStatus().equals("Pending")){
            throw new ApiException("Post with this"+postId+"is not Pending");
        }

        Border border = borderRepository.findBorderByName(addPostDTO.getBorderName());
        if (border == null) {
            throw new ApiException("Border not found with the name: " + addPostDTO.getBorderName());
        }

        post.setTitle(addPostDTO.getTitle());
        post.setCategory(addPostDTO.getCategory());
        post.setShipmentType(addPostDTO.getShipmentType());
        post.setCountryOfOrigin(addPostDTO.getCountryOfOrigin());
        post.setWeight(addPostDTO.getWeight());
        post.setHasDocuments(addPostDTO.getHasDocuments());
        post.setHasDelivery(addPostDTO.getHasDelivery());
        post.setBillOfLading(addPostDTO.getBillOfLading());
        post.setCustomer(customer); // Set the customer
        post.setBorder(border);     // Set the border
        postRepository.save(post);


    }

    public void deletePost(Integer postId,Integer customerId){
    Post post = postRepository.findPostById(postId);

    if(post==null){
        throw new ApiException("Post with this"+postId+" does not exist");
    }

    if (!post.getStatus().equals("Pending")){
        throw new ApiException("Post with this"+postId+"is not Pending");
    }

    Customer customer=customerRepository.findCustomerById(customerId);

    if(customer==null){
        throw new ApiException("Customer with this"+customerId+" does not exist");
    }
    postRepository.delete(post);
    }



    // broker
    public void sendPostForOnoBroker(Integer customerId,Integer brokerId, AddPostDTO addPostDTO) {
        // Validate the broker
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);

        MyUser customer = authRepository.findMyUserById(customerId);


        if (customer == null)
            throw new ApiException("Customer with this"+customerId+" does not exist");

        if(broker==null)
            throw new ApiException("Broker with ID " + brokerId + " not found.");




        Border border = borderRepository.findBorderByName(addPostDTO.getBorderName());
        if (border == null)
            throw new ApiException("Border not found with the name: " + addPostDTO.getBorderName());


        // Create a new Post entity
        Post post = new Post();
        post.setTitle(addPostDTO.getTitle());
        post.setCategory(addPostDTO.getCategory());
        post.setShipmentType(addPostDTO.getShipmentType());
        post.setCountryOfOrigin(addPostDTO.getCountryOfOrigin());
        post.setWeight(addPostDTO.getWeight());
        post.setHasDocuments(addPostDTO.getHasDocuments());
        post.setHasDelivery(addPostDTO.getHasDelivery());
        post.setBillOfLading(addPostDTO.getBillOfLading());
        post.setBorder(border);

        post.setCustomer(customer.getCustomer());
        post.setCustomsBrokers(broker);

        // Save the new post
        postRepository.save(post);

        Rental rental = rentalRepository.findRentalByCustomerIdAndAndBrokerIdAndStatus(customerId,brokerId,"Active");

        if (rental != null && rental.getNumberOfOrder() >0){
            // Create the offer
            Offer offer = new Offer();
            offer.setPost(post);
            offer.setPrice(rental.getSupPrice());
            offer.setBroker(broker);

            if (post.getHasDelivery()) {
                offer.setDeliveryIncluded(true);
                offer.setDeliveryPrice(rental.getPriceDelivery());
            } else {
                offer.setDeliveryIncluded(false);
            }
            offerRepository.save(offer);

            // Create a new order
            Orders order = new Orders();
            order.setStatus("Placed");
            order.setPaymentStatus("Pending");
            order.setOffer(offer);

            // Associate the offer with the order
            offer.setOrder(order);
            offer.setOfferStatus("Accepted");
            offer.getPost().setStatus("Accepted");

            // Save the order and update the offer
            ordersRepository.save(order);
            offerRepository.save(offer);

            rental.setNumberOfOrder(rental.getNumberOfOrder()-1);
            rentalRepository.save(rental);

        } else if (rental != null && rental.getNumberOfOrder() == 0) {
            rental.setStatus("Expired");
            rentalRepository.save(rental);
            String customerEmail = customer.getEmail();
            String brokerEmail = broker.getUser().getEmail();

            String subject = "إنتهاء العقد - لا يوجد شحنات متبقية";
            String body = "<html><body>" +
                    "<p>عزيزي/عزيزتي " + customer.getFullName() + "،</p>" +
                    "<p>نود إعلامك بأن العقد الخاص بك قد انتهى، حيث تم الوصول إلى الصفر في عدد الشحنات المتاحة.</p>" +
                    "<p>شكرًا لاستخدامك خدماتنا.</p>" +
                    "<p>مع أطيب التحيات،</p>" +
                    "<p>فريق بيان</p>" +
                    "</body></html>";

            try {
                // Send emails to both customer and broker
                emailService.sendEmail(customerEmail, subject, body);
                emailService.sendEmail(brokerEmail, subject, body);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }



    public List<PostDTO> getPostsForBroker(Integer brokerId) {
        // Validate the broker
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        // Fetch all posts associated with the broker
        List<Post> posts = postRepository.findPostsByCustomsBrokers(broker);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the broker with ID " + brokerId);
        }

        // Map Post entities to PostDTO objects
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            dto.setTitle(post.getTitle());
            dto.setCategory(post.getCategory());
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

    public List<PostDTO> getAllPosts(){
      List<Post> posts = postRepository.findPostsByStatus("Pending");

        if (posts.isEmpty()) {
            throw new ApiException("No posts found");
        }
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            dto.setTitle(post.getTitle());
            dto.setCategory(post.getCategory());
            dto.setWeight(post.getWeight());
            dto.setShipmentType(post.getShipmentType());
            dto.setCountryOfOrigin(post.getCountryOfOrigin());
            dto.setBorderName(post.getBorder().getName());
            postDTOS.add(dto);
        }
        return postDTOS;
    }
    //****** 6 filter for one page and any one can see it


    // **All Posts By Category (Excluding Posts with a Broker)
    public List<Post> getAllPostByCategory(String category) {
        List<Post> posts = postRepository.findPostByCategory(category);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the category " + category);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the category " + category);
        }

        return filteredPosts;
    }


    // **All Posts By Country Of Origin (Excluding Posts with a Broker)
    public List<Post> getAllPostByCountryOfOrigin(String countryOfOrigin) {
        List<Post> posts = postRepository.findPostByCountryOfOrigin(countryOfOrigin);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified country of origin: " + countryOfOrigin);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified country of origin: " + countryOfOrigin);
        }

        return filteredPosts;
    }

    // **All Posts By Shipment Type (Excluding Posts with a Broker)
    public List<Post> getAllPostByShipmentType(String shipmentType) {
        List<Post> posts = postRepository.findPostByShipmentType(shipmentType);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified shipment type: " + shipmentType);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified shipment type: " + shipmentType);
        }

        return filteredPosts;
    }

    // **All Posts By Category And Country Of Origin (Excluding Posts with a Broker)
    public List<Post> getAllPostByTheCategoryAndCountryOfOrigin(String category, String countryOfOrigin) {
        List<Post> posts = postRepository.findPostByCategoryAndCountryOfOrigin(category, countryOfOrigin);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified category: " + category + " or country of origin: " + countryOfOrigin);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified category: " + category + " and country of origin: " + countryOfOrigin);
        }

        return filteredPosts;
    }

    // **All Posts By Category And Shipment Type (Excluding Posts with a Broker)
    public List<Post> getAllPostByCategoryAndShipmentType(String category, String shipmentType) {
        List<Post> posts = postRepository.findPostByCategoryAndShipmentType(category, shipmentType);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified category: " + category + " or shipment type: " + shipmentType);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified category: " + category + " and shipment type: " + shipmentType);
        }

        return filteredPosts;
    }

    // **All Posts By Category, Shipment Type, And Country Of Origin (Excluding Posts with a Broker)
    public List<Post> getAllPostByCategoryAndShipmentTypeAndCountryOfOrigin(String category, String shipmentType, String countryOfOrigin) {
        List<Post> posts = postRepository.findPostByCategoryAndShipmentTypeAndCountryOfOrigin(category, shipmentType, countryOfOrigin);

        if (posts.isEmpty()) {
            throw new ApiException("No posts found for the specified category: " + category + ", shipment type: " + shipmentType + ", or country of origin: " + countryOfOrigin);
        }

        // Filter out posts that have a broker assigned
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCustomsBrokers() == null)
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new ApiException("No posts without brokers found for the specified category: " + category + ", shipment type: " + shipmentType + ", and country of origin: " + countryOfOrigin);
        }

        return filteredPosts;
    }

    // ميثود 1: إحصائيات الإعلانات العامة للعميل
    public Map<String, Object> getGeneralPostStatisticsForCustomer(Integer customerId) {

        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }

        List<Post> posts = postRepository.findAllByCustomer_Id(customerId);

        int totalPosts = posts.size();
        int pendingPosts = (int) posts.stream().filter(post -> "Pending".equals(post.getStatus())).count();
        int acceptedPosts = (int) posts.stream().filter(post -> "Accepted".equals(post.getStatus())).count();
        int canceledPosts = (int) posts.stream().filter(post -> "Canceled".equals(post.getStatus())).count();

        // عدد الإعلانات التي تحتوي على عروض
        int postsWithOffers = (int) posts.stream().filter(post -> post.getOffers() != null && !post.getOffers().isEmpty()).count();


        Map<String, Object> stats = new HashMap<>();
        stats.put("Total Posts", totalPosts);
        stats.put("Pending Posts", pendingPosts);
        stats.put("Accepted Posts", acceptedPosts);
        stats.put("Canceled Posts", canceledPosts);
        stats.put("Posts with Offers", postsWithOffers);

        return stats;
    }



    public Map<String, Object> getOfferDetailsForPost(Integer postId, Integer customerId) {


        Post post = postRepository.findPostByIdAndCustomerId(postId, customerId);
        if (post == null) {
            throw new ApiException("Post with ID " + postId + " does not belong to the customer with ID " + customerId);
        }


        int totalOffers = post.getOffers().size();

        if (totalOffers == 0) {
            throw new ApiException("No offers found for Post ID " + postId);
        }

        BigDecimal totalPriceSum = BigDecimal.ZERO;
        BigDecimal highestPrice = BigDecimal.ZERO;
        BigDecimal lowestPrice = null;

        for (Offer offer : post.getOffers()) {
            BigDecimal price = offer.getPrice();

            totalPriceSum = totalPriceSum.add(price);


            if (price.compareTo(highestPrice) > 0) {
                highestPrice = price;
            }

            if (lowestPrice == null || price.compareTo(lowestPrice) < 0) {
                lowestPrice = price;
            }
        }

        BigDecimal averagePrice = totalPriceSum.divide(BigDecimal.valueOf(totalOffers), 2, RoundingMode.HALF_UP);

        Map<String, Object> stats = new HashMap<>();
        stats.put("Total Offers", totalOffers);
        stats.put("Average Price", averagePrice);
        stats.put("Highest Price", highestPrice);
        stats.put("Lowest Price", lowestPrice);


        return stats;
    }

}
