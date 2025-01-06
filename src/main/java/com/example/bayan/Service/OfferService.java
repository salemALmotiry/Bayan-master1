package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.Offer.OfferDTO;
import com.example.bayan.DTO.IN.Offer.OfferWithDeliveryDTO;
import com.example.bayan.DTO.OUT.CustomerOfferDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {


    private final AuthRepository authRepository;
    private final PostRepository postRepository;
    private final CustomBrokerRepository customBrokerRepository;
    private final OfferRepository offerRepository;
    private final OrdersRepository ordersRepository;
    private final CustomerRepository customerRepository;
    private final  RentalRepository rentalRepository;

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final SubscriptionPostRepository subscriptionPostRepository;

    // ____________________Broker Offer Without Delivery________________
    public void createOffer(Integer userId, OfferDTO offerDTO) {
        // Validate the broker
        MyUser broker = authRepository.findMyUserById(userId);
        if (broker == null)
            throw new ApiException("Broker with ID " + userId + " not found.");

        // Validate the post
        Post post = postRepository.findPostById(offerDTO.getPostId());
        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        // Ensure the post status is pending
        if (!"Pending".equalsIgnoreCase(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is not available for offers (current status: " + post.getStatus() + ").");
        }

        // Ensure there isn't already an existing pending offer for the same broker and post
        if (offerRepository.existsByPostAndBrokerAndOfferStatus(post, broker.getBroker(), "Pending")) {
            throw new ApiException("A pending offer already exists for this post and broker.");
        }


        // Create the offer
        Offer offer = new Offer();
        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice());
        offer.setBroker(customBrokerRepository.findCustomsBrokerById(userId));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(false);

        offerRepository.save(offer);
    }

    public void updateOffer(Integer userId,Integer offerId, OfferDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);


        if (myUser == null){
            throw new ApiException("Broker not found");
        }

        Post post = postRepository.findPostById(offerDTO.getPostId());

        if (post == null){
            throw new ApiException("Post not found");
        }

        if (!post.getStatus().equals("Pending")){
            throw new ApiException("Post is taken by broker");
        }


        Offer offer = offerRepository.findOfferByIdAndBrokerId(offerId,userId);

        if (offer == null){
            throw new ApiException("Offer not found");
        }
        if (!offer.getOfferStatus().equals("Pending")){
            throw new ApiException("Offer is not Pending");
        }
        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice());
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(false);

        offerRepository.save(offer);



    }

    public void deleteOffer(Integer userId,Integer offerId) {
        MyUser broker = authRepository.findMyUserById(userId);

        if(broker==null){
            throw new ApiException("Broker with this"+userId+" does not exist");
        }

        Offer offer=offerRepository.findOfferByIdAndBrokerId(offerId,userId);

        if(offer==null){
            throw new ApiException("Offer with this"+offerId+" does not exist");
        }

        if (!offer.getOfferStatus().equals("Pending")){
            throw new ApiException("Offer is not Pending");
        }

        offerRepository.delete(offer);
    }

    // ____________________broker offer with delivery________________
    public void createOfferWithDelivery(Integer userId, OfferWithDeliveryDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);

        if (myUser == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Post post = postRepository.findPostById(offerDTO.getPostId());

        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        // Ensure the post status is pending
        if (!"Pending".equalsIgnoreCase(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is not available for offers (current status: " + post.getStatus() + ").");
        }

        // Ensure there isn't already an existing pending offer for the same broker and post
        if (offerRepository.existsByPostAndBrokerAndOfferStatus(post, myUser.getBroker(), "Pending")) {
            throw new ApiException("A pending offer already exists for this post and broker.");
        }

        if (!post.getHasDelivery()){
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " has no delivery.");
        }

        Offer offer = new Offer();
        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice().add(offerDTO.getDeliveryPrice()));
        offer.setBroker(customBrokerRepository.findCustomsBrokerById(userId));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(true);
        offer.setDeliveryPrice(offerDTO.getDeliveryPrice());

        offerRepository.save(offer);
    }

    public void updateOfferWithDelivery(Integer userId, Integer offerId, OfferWithDeliveryDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);

        if (myUser == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Post post = postRepository.findPostById(offerDTO.getPostId());

        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        if (!"Pending".equals(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is already taken by another broker.");
        }

        Offer offer = offerRepository.findOfferByIdAndBrokerId(offerId,userId);

        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " is not in Pending status.");
        }

        offer.setPost(post);
        offer.setPrice(offerDTO.getPrice().add(offerDTO.getDeliveryPrice()));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(true);
        offer.setDeliveryPrice(offerDTO.getDeliveryPrice());

        offerRepository.save(offer);
    }

    public void removeOffer(Integer userId, Integer offerId) {
        MyUser broker = authRepository.findMyUserById(userId);

        if (broker == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        Offer offer = offerRepository.findOfferByIdAndBrokerId(offerId,userId);

        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " is not in Pending status and cannot be deleted.");
        }

        offerRepository.delete(offer);
    }

    //___________________________sub offer_________________________________________


    public void createSubOffer(Integer userId, OfferDTO offerDTO) {
        // Validate the broker
        MyUser broker = authRepository.findMyUserById(userId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        // Validate the post
        SubscriptionPost post = subscriptionPostRepository.findSubscriptionPostById(offerDTO.getPostId());
        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        // Ensure the post status is pending
        if (!"Pending".equalsIgnoreCase(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is not available for offers (current status: " + post.getStatus() + ").");
        }

        // Ensure there isn't already an existing pending offer for the same broker and post
        if (offerRepository.existsBySubscriptionPostAndBrokerAndOfferStatus(post, broker.getBroker(), "Pending")) {
            throw new ApiException("A pending offer already exists for this post and broker.");
        }

        // Create the offer
        Offer offer = new Offer();
        offer.setSubscriptionPost(post);
        offer.setPrice(offerDTO.getPrice());
        offer.setBroker(customBrokerRepository.findCustomsBrokerById(userId));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(false);

        offerRepository.save(offer);
    }

    public void updateSubOffer(Integer userId,Integer offerId, OfferDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);


        if (myUser == null){
            throw new ApiException("Broker not found");
        }

        SubscriptionPost post = subscriptionPostRepository.findSubscriptionPostById(offerDTO.getPostId());

        if (post == null){
            throw new ApiException("Post not found");
        }

        if (!post.getStatus().equals("Pending")){
            throw new ApiException("Post is taken by broker");
        }


        Offer offer = offerRepository.findOfferByIdAndBrokerId(offerId,userId);

        if (offer == null){
            throw new ApiException("Offer not found");
        }
        if (!offer.getOfferStatus().equals("Pending")){
            throw new ApiException("Offer is not Pending");
        }
        offer.setSubscriptionPost(post);
        offer.setPrice(offerDTO.getPrice());
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(false);

        offerRepository.save(offer);



    }


    // ____________________broker offer with delivery________________
    public void createSubOfferWithDelivery(Integer userId, OfferWithDeliveryDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);

        if (myUser == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        SubscriptionPost post = subscriptionPostRepository.findSubscriptionPostById(offerDTO.getPostId());

        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        // Ensure the post status is pending
        if (!"Pending".equalsIgnoreCase(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is not available for offers (current status: " + post.getStatus() + ").");
        }

        // Ensure there isn't already an existing pending offer for the same broker and post
        if (offerRepository.existsBySubscriptionPostAndBrokerAndOfferStatus(post, myUser.getBroker(), "Pending")) {
            throw new ApiException("A pending offer already exists for this post and broker.");
        }

        if (!post.getHasDelivery()){
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " has no delivery.");
        }


        Offer offer = new Offer();
        offer.setSubscriptionPost(post);
        offer.setPrice(offerDTO.getPrice().add(offerDTO.getDeliveryPrice()));
        offer.setBroker(customBrokerRepository.findCustomsBrokerById(userId));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(true);
        offer.setDeliveryPrice(offerDTO.getDeliveryPrice());

        offerRepository.save(offer);
    }

    public void updateSubOfferWithDelivery(Integer userId, Integer offerId, OfferWithDeliveryDTO offerDTO) {
        MyUser myUser = authRepository.findMyUserById(userId);

        if (myUser == null) {
            throw new ApiException("Broker with ID " + userId + " not found.");
        }

        SubscriptionPost post = subscriptionPostRepository.findSubscriptionPostById(offerDTO.getPostId());


        if (post == null) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " not found.");
        }

        if (!"Pending".equals(post.getStatus())) {
            throw new ApiException("Post with ID " + offerDTO.getPostId() + " is already taken by another broker.");
        }

        Offer offer = offerRepository.findOfferByIdAndBrokerId(offerId,userId);

        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " is not in Pending status.");
        }

        offer.setSubscriptionPost(post);
        offer.setPrice(offerDTO.getPrice().add(offerDTO.getDeliveryPrice()));
        offer.setOfferStatus("Pending");
        offer.setDeliveryIncluded(true);
        offer.setDeliveryPrice(offerDTO.getDeliveryPrice());

        offerRepository.save(offer);
    }



    //_______________________________________________________________

    public void acceptOffer(Integer userId, Integer offerId) {
        // Validate the user
        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + userId + " not found.");
        }

        // Validate the offer
        Offer offer = offerRepository.findOfferByIdAndPost_CustomerId(offerId, customer.getId());
        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        System.out.println(offer.getOfferStatus());
        // Check if the offer is already accepted
        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " cannot be accepted as it is not pending.");
        }

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

        List<Offer> offers = offerRepository.findAllByPostId(offer.getPost().getId());
        for (Offer offer1 : offers) {
            if (!offer1.getOfferStatus().equals("Accepted")) {
                offer1.setOfferStatus("Rejected");
                offerRepository.save(offer1);
            }
        }

        // Notification for the customer
        Notification notification = new Notification();
        notification.setMassage("لقد قبلت هذا العرض");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(customer);
        notificationRepository.save(notification);

        // Notification for the broker whose offer has been accepted
        Notification notification2 = new Notification();
        notification2.setMassage("لقد تم قبول عرضك");
        notification2.setCreateAt(LocalDateTime.now());
        notification2.setMyUser(offer.getBroker().getUser());
        notificationRepository.save(notification2);

        // Send email notifications
        sendAcceptanceEmailToBroker(offer);
        sendAcceptanceEmailToCustomer(offer);

        // Send WhatsApp messages
        String customerPhone = customer.getPhoneNumber();
        String brokerPhone = offer.getBroker().getUser().getPhoneNumber();

        String customerMessage = "مرحبًا " + customer.getFullName() + "،\n\n" +
                "لقد قمت بقبول العرض التالي:\n" +
                "- السعر: " + offer.getPrice() + " ريال\n" +
                "- حالة الطلب: " + "بأنتظار التخليص" + "\n\n" +
                "شكرًا لاستخدامك خدماتنا.\n\n" +
                "مع تحيات فريقنا.";

        String brokerMessage = "مرحبًا " + offer.getBroker().getUser().getFullName() + "،\n\n" +
                "لقد تم قبول عرضك التالي:\n" +

                "- السعر: " + offer.getPrice() + " ريال\n" +
                "- حالة الطلب: " + "بأنتظار التخليص" + "\n\n" +
                "شكرًا لتعاونك معنا.\n\n" +
                "مع تحيات فريقنا.";

        sendWhatsAppMessage(customerPhone, customerMessage);
        sendWhatsAppMessage(brokerPhone, brokerMessage);
    }

    //________________________________SubscriptionPost____________________________________

    public void acceptOfferForSubscriptionPost(Integer userId, Integer offerId) {

        // Validate customer
        MyUser customer = authRepository.findMyUserById(userId);
        if (customer == null) {
            throw new ApiException("Customer with ID " + userId + " not found.");
        }

        // Validate offer
        Offer offer = offerRepository.findOfferByIdAndSupPost_CustomerId(offerId, customer.getId());
        if (offer == null) {
            throw new ApiException("Offer with ID " + offerId + " not found.");
        }

        if (!"Pending".equals(offer.getOfferStatus())) {
            throw new ApiException("Offer with ID " + offerId + " cannot be accepted as it is not pending.");
        }

        // Validate subscription post
        SubscriptionPost post = offer.getSubscriptionPost();
        if (post == null) {
            throw new ApiException("Offer is not associated with any SubscriptionPost.");
        }

        if (!post.getStatus().equals("Pending")) {
            throw new ApiException("Post with ID " + offerId + " is not in Pending status.");
        }

        // Create rental
        Rental rental = new Rental();
        rental.setNumberOfOrder(post.getShipmentsNumber());
        rental.setSupPrice(offer.getPrice());

        BigDecimal price = offer.getPrice() != null ? offer.getPrice() : BigDecimal.ZERO;
        Integer shipmentsNumber = post.getShipmentsNumber() != null ? post.getShipmentsNumber() : 0;
        BigDecimal priceDelivery = rental.getPriceDelivery() != null ? rental.getPriceDelivery() : BigDecimal.ZERO;
        BigDecimal totalPrice = price
                .multiply(BigDecimal.valueOf(shipmentsNumber))
                .add(priceDelivery);

        rental.setTotalPrice(totalPrice);

        rental.setPriceDelivery(offer.getDeliveryPrice());
        rental.setStartDateTime(LocalDateTime.now());
        rental.setBroker(offer.getBroker());
        rental.setHasDelivery(offer.getDeliveryIncluded());
        rental.setCustomer(customer.getCustomer());
        rental.setStatus("Active");

        // Update offer and post
        offer.setOfferStatus("Accepted");
        post.setStatus("Accepted");
        rentalRepository.save(rental);
        offerRepository.save(offer);
        subscriptionPostRepository.save(post);

        // Reject other offers for the same subscription post
        List<Offer> offers = offerRepository.findAllBySubscriptionPostId(post.getId());
        for (Offer offer1 : offers) {
            if (!offer1.getOfferStatus().equals("Accepted")) {
                offer1.setOfferStatus("Rejected");
                offerRepository.save(offer1);
            }
        }

        String customerEmailText = "<html><body>" +
                "<p>عزيزي " + customer.getFullName() + "،</p>" +
                "<p>تم قبول العرض الخاص بك للإعلان التالي:</p>" +
                "<ul>" +
                "<li>عنوان الإعلان: " + post.getTitle() + "</li>" +
                "<li>عدد الشحنات: " + rental.getNumberOfOrder() + "</li>" +
                "<li>السعر لكل شحنة: " + rental.getSupPrice() + " ريال</li>" +
                "<li>السعر الإجمالي: " + rental.getTotalPrice() + " ريال</li>" +
                "<li>تاريخ البدء: " + rental.getStartDateTime() + "</li>" +
                "<li>حالة العقد: " + rental.getStatus() + "</li>" +
                "</ul>" +
                "<p>شكرًا لاستخدامك خدماتنا.</p>" +
                "<p>مع أطيب التحيات،</p>" +
                "<p>فريق بيان</p>" +
                "</body></html>";

        String brokerEmailText = "<html><body>" +
                "<p>عزيزي " + offer.getBroker().getUser().getFullName() + "،</p>" +
                "<p>تم قبول العرض الذي قدمته للإعلان التالي:</p>" +
                "<ul>" +
                "<li>عنوان الإعلان: " + post.getTitle() + "</li>" +
                "<li>عدد الشحنات: " + rental.getNumberOfOrder() + "</li>" +
                "<li>السعر لكل شحنة: " + rental.getSupPrice() + " ريال</li>" +
                "<li>السعر الإجمالي: " + rental.getTotalPrice() + " ريال</li>" +
                "<li>تاريخ البدء: " + rental.getStartDateTime() + "</li>" +
                "<li>حالة العقد: " + rental.getStatus() + "</li>" +
                "</ul>" +
                "<p>شكرًا لتعاونك معنا.</p>" +
                "<p>مع أطيب التحيات،</p>" +
                "<p>فريق بيان</p>" +
                "</body></html>";

        sendEmail(customer.getEmail(), "تم قبول العرض وإنشاء العقد", customerEmailText);
        sendEmail(offer.getBroker().getUser().getEmail(), "تم قبول العرض وإنشاء العقد", brokerEmailText);
    }



    private void sendEmail(String to, String subject, String text) {
        try {
            emailService.sendEmail(to, subject, text);
        } catch (MessagingException e) {
            e.printStackTrace();
        } }


    private void sendAcceptanceEmailToCustomer(Offer offer) {
        String subject = "تم قبول العرض: " + offer.getPost().getTitle();

        // نص البريد بصيغة HTML
        String body = "<html><body>" +
                "<p>عزيزي " + offer.getPost().getCustomer().getUser().getFullName() + "،</p>" +
                "<p>تم قبول عرضك للإعلان بعنوان '" + offer.getPost().getTitle() + "'.</p>" +
                "<p><strong>تفاصيل الطلب:</strong></p>" +
                "<ul>" +
                "<li><strong>سعر العرض:</strong> " + offer.getPrice() + " ريال سعودي</li>" +
                "<li><strong>حالة الطلب:</strong> تم وضعه</li>" +
                "<li><strong>حالة الدفع:</strong> قيد الانتظار</li>" +
                "</ul>" +
                "<p>شكرًا لاستخدامك خدماتنا!</p>" +
                "<p>أطيب التحيات،</p>" +
                "<p>فريق بيان</p>" +
                "</body></html>";


        try {
            emailService.sendEmail(offer.getPost().getCustomer().getUser().getEmail(), subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendAcceptanceEmailToBroker(Offer offer) {
        String subject = "تم قبول عرضك!";
        String body = "<html><body>" +
                "<p>عزيزي " + offer.getBroker().getUser().getFullName() + "،</p>" +
                "<p>تهانينا! تم قبول عرضك للإعلان بعنوان '" + offer.getPost().getTitle() + "'.</p>" +
                "<p><strong>تفاصيل العرض:</strong></p>" +
                "<ul>" +
                "<li><strong>سعر العرض:</strong> " + offer.getPrice() + " ريال سعودي</li>" +
                "<li><strong>اسم العميل:</strong> " + offer.getBroker().getUser().getFullName() + "</li>" +
                "<li><strong>حالة الطلب:</strong> تم وضعه</li>" +
                "<li><strong>حالة الدفع:</strong> قيد الانتظار</li>" +
                "</ul>" +
                "<p>يرجى متابعة الإجراءات التالية بناءً على الطلب.</p>" +
                "<p>أطيب التحيات،</p>" +
                "<p>فريق بيان</p>" +
                "</body></html>";

        try {
            emailService.sendEmail(offer.getPost().getCustomer().getUser().getEmail(), subject, body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //EndPPoint for Customer he can see all The Offer for his post
    public List<CustomerOfferDTO> getAllOffersForOnePost(Integer postID, Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null) {
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }

        Post post = postRepository.findPostByIdAndCustomerId(postID,customerId);
        if (post == null) {
            throw new ApiException("Post with ID " + postID + " not found");
        }

        List<Offer> offers = offerRepository.getAllOfferByPostAndPostCustomerId(post,customerId);

        return offers.stream()
                .map(offer -> {
                    CustomerOfferDTO dto = new CustomerOfferDTO();
                    dto.setFullName(offer.getBroker().getUser().getFullName());
                    dto.setCompanyName(offer.getBroker().getCompanyName());
                    dto.setPrice(offer.getPrice());
                    return dto;
                })
                .sorted(Comparator.comparing(CustomerOfferDTO::getPrice))
                .toList();
    }

    public List<CustomerOfferDTO> getAllOffersForOneSubPost(Integer postID, Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null) {
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }

        SubscriptionPost post = subscriptionPostRepository.findPostByIdAndCustomerId(postID,customerId);
        if (post == null) {
            throw new ApiException("Post with ID " + postID + " not found");
        }

        List<Offer> offers = offerRepository.getAllOfferBySubscriptionPostAndSubscriptionPostCustomerId(post,customerId);

        return offers.stream()
                .map(offer -> {
                    CustomerOfferDTO dto = new CustomerOfferDTO();
                    dto.setFullName(offer.getBroker().getUser().getFullName());
                    dto.setCompanyName(offer.getBroker().getCompanyName());
                    dto.setPrice(offer.getPrice());
                    return dto;
                })
                .sorted(Comparator.comparing(CustomerOfferDTO::getPrice))
                .toList();
    }

    private void sendWhatsAppMessage(String to, String message) {
        try {
            Unirest.setTimeouts(5000, 10000); // 5 seconds to connect, 10 seconds for response
            HttpResponse<String> response = Unirest.post("https://api.ultramsg.com/instance103253/messages/chat")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("token", "66e2lh7c7hkrtj3o")
                    .field("to", to)
                    .field("body", message)
                    .asString();

            System.out.println("WhatsApp message sent successfully: " + response.getBody());
        } catch (Exception e) {
            throw new ApiException("Failed to send WhatsApp message: " + e.getMessage());
        }
    }

}
