package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.CustomsBrokerDTO;
import com.example.bayan.DTO.IN.UpdateCustomsBrokerDTO;
import com.example.bayan.DTO.OUT.CustomBrokerDTO;
import com.example.bayan.DTO.OUT.CustomBrokerFilterDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.BorderRepository;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import com.example.bayan.Repostiry.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class
CustomBrokerService {


    private final CustomBrokerRepository customBrokerRepository;
    private final AuthRepository authRepository;
    private final BorderRepository borderRepository;
    private final NotificationRepository notificationRepository;


    // register new Custom Broker
    public void register(CustomsBrokerDTO customsBrokerDTO){

        MyUser broker = new MyUser();
        broker.setUsername(customsBrokerDTO.getUsername());
        broker.setPassword(new BCryptPasswordEncoder().encode(customsBrokerDTO.getPassword()));
        broker.setFullName(customsBrokerDTO.getFullName());
        broker.setEmail(customsBrokerDTO.getEmail());
        broker.setPhoneNumber(customsBrokerDTO.getPhoneNumber());
        broker.setRole("UNACTIVE");
        broker.setCreatedAt(LocalDateTime.now());
        authRepository.save(broker);

        CustomsBroker customsBroker = new CustomsBroker();
        customsBroker.setCommercialLicense(customsBrokerDTO.getCommercialLicense());
        customsBroker.setLicenseNumber(customsBrokerDTO.getLicenseNumber());
        customsBroker.setLicenseType(customsBrokerDTO.getLicenseType());
        customsBroker.setCompanyName(customsBrokerDTO.getCompanyName());
        customsBroker.setUser(broker);

        Set<Border> borders = customsBrokerDTO.getBorderIds().stream()
                .map(borderId -> borderRepository.findById(borderId)
                        .orElseThrow(() -> new ApiException("Border with ID " + borderId + " not found")))
                .collect(Collectors.toSet());
        customsBroker.setBorders(borders);
        customBrokerRepository.save(customsBroker);

    }

    public void updateBrokerBorders(Integer brokerId, List<Integer> borderIds) {
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        List<Border> borders = borderIds.stream()
                .map(id -> borderRepository.findById(id)
                        .orElseThrow(() -> new ApiException("Border with ID " + id + " not found.")))
                .collect(Collectors.toList());

        broker.setBorders(new HashSet<>(borders));
        customBrokerRepository.save(broker);
    }


    public void removeBrokerBorder(Integer brokerId, Integer borderId) {
        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        Border border = borderRepository.findById(borderId)
                .orElseThrow(() -> new ApiException("Border with ID " + borderId + " not found."));

        if (!broker.getBorders().contains(border)) {
            throw new ApiException("Border with ID " + borderId + " is not associated with this broker.");
        }

        broker.getBorders().remove(border); // إزالة المنفذ
        customBrokerRepository.save(broker);
    }

    // get .. MyProfile .. Custom Broker
    public CustomBrokerDTO myProfile(Integer broker_id){
      MyUser user = authRepository.findMyUserById(broker_id);
      if (user==null){
          throw new ApiException("Broker Id is wrong");
      }
      CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(broker_id);
       if(broker==null){
           throw new ApiException("Wrong broker Id");
       }

      return new CustomBrokerDTO(user.getUsername(),user.getEmail(),user.getPhoneNumber(), user.getFullName(), broker.getLicenseNumber(),broker.getCompanyName(),broker.getCommercialLicense(),broker.getLicenseType());
    }

    // update
    public void updateMyAccount(Integer broker_id , UpdateCustomsBrokerDTO customsBrokerDTO){
        MyUser oldBroker = authRepository.findMyUserById(broker_id);
        if (oldBroker==null){
            throw new ApiException("Broker Id is wrong");
        }

        oldBroker.setUsername(customsBrokerDTO.getUsername());

        oldBroker.setFullName(customsBrokerDTO.getFullName());
        oldBroker.setEmail(customsBrokerDTO.getEmail());
        oldBroker.setPhoneNumber(customsBrokerDTO.getPhoneNumber());
        oldBroker.setUpdatedAt(LocalDateTime.now());
        authRepository.save(oldBroker);

        CustomsBroker customsBroker = oldBroker.getBroker();
        customsBroker.setCommercialLicense(customsBrokerDTO.getCommercialLicense());
        customsBroker.setLicenseNumber(customsBrokerDTO.getLicenseNumber());
        customsBroker.setLicenseType(customsBrokerDTO.getLicenseType());
        customsBroker.setCompanyName(customsBrokerDTO.getCompanyName());
        customBrokerRepository.save(customsBroker);
    }

    // delete
    public void deleteMyAccount(Integer broker_id){
        MyUser broker = authRepository.findMyUserById(broker_id);
        if (broker==null||!broker.getBroker().getIsActive()){
            throw new ApiException("Broker Id is wrong or broker is not active");
        }
    authRepository.delete(broker);
    }

    // get all custom brokers with rating for the customer
    public List<CustomBrokerFilterDTO> getAllCustomsBrokers() {
        List<CustomsBroker> customsBrokers = customBrokerRepository.findAll();

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No customs brokers found in the system.");
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }

    // *** EndPoint to Get Customs Broker By License Number
    public CustomBrokerFilterDTO getByLicenseNumber(String lNumber) {
        CustomsBroker customsBroker = customBrokerRepository.getCustomsBrokersByLicenseNumber(lNumber);

        if (customsBroker == null||!customsBroker.getIsActive()) {
            throw new ApiException("No Customs Broker found with license number: " + lNumber);
        }

        return mapToCustomBrokerFilterDTO(customsBroker);
    }

    // *** EndPoint to Get All Customs Brokers Working at a Specific Border
    public List<CustomBrokerFilterDTO> getAllCustomsByBorder(String border) {
        List<CustomsBroker> customsBrokers = customBrokerRepository.findByBorderName(border);

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No Customs Brokers found for the border: " + border);
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }

    // *** EndPoint to Get All Customs Brokers By Name
    public List<CustomBrokerFilterDTO> getAllCustomsByName(String name) {
        List<CustomsBroker> customsBrokers = customBrokerRepository.getCustomsBrokerByUserFullName(name);

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No Customs Brokers found with the name: " + name);
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }

    // *** EndPoint to Get All Customs Brokers By License Type
    public List<CustomBrokerFilterDTO> getAllCustomsByLicenseType(String type) {
        List<CustomsBroker> customsBrokers = customBrokerRepository.getCustomsBrokerByLicenseType(type);

        if (customsBrokers.isEmpty()) {
            throw new ApiException("No Customs Brokers found with license type: " + type);
        }

        return customsBrokers.stream()
                .map(this::mapToCustomBrokerFilterDTO)
                .collect(Collectors.toList());
    }


    // Helper method to map CustomsBroker entity to CustomBrokerFilterDTO
    private CustomBrokerFilterDTO mapToCustomBrokerFilterDTO(CustomsBroker customsBroker) {
        return new CustomBrokerFilterDTO(
                customsBroker.getUser().getFullName(),
                customsBroker.getLicenseNumber(),
                customsBroker.getCommercialLicense(),
                customsBroker.getLicenseType()
        );
    }

    public Map<String, Object> getOfferStatisticsForBroker(Integer brokerId) {

        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if(broker==null){
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }
        Set<Offer> offers = broker.getOffers();

        int totalOffers = offers.size();
        int acceptedOffers = (int) offers.stream().filter(offer -> "Accepted".equals(offer.getOfferStatus())).count();
        int rejectedOffers = (int) offers.stream().filter(offer -> "Rejected".equals(offer.getOfferStatus())).count();
        int pendingOffers = (int) offers.stream().filter(offer -> "Pending".equals(offer.getOfferStatus())).count();
        int canceledOffers = (int) offers.stream().filter(offer -> "Canceled".equals(offer.getOfferStatus())).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("Total Offers", totalOffers);
        stats.put("Accepted Offers", acceptedOffers);
        stats.put("Rejected Offers", rejectedOffers);
        stats.put("Pending Offers", pendingOffers);
        stats.put("Canceled Offers", canceledOffers);

        return stats;
    }

    // ميثود 2: إحصائيات التقييمات للمخلص الجمركي
    public Map<String, Object> getReviewStatisticsForBroker(Integer brokerId) {

        CustomsBroker broker = customBrokerRepository.findCustomsBrokerById(brokerId);
        if(broker==null){
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }

        Set<Review> reviews = broker.getReceivedReviews();

        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        int totalReviews = reviews.size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("Total Reviews", totalReviews);
        stats.put("Average Rating", averageRating);

        return stats;
    }

    
    
  ////////////////Broker get All Notification

    public List<Notification>getAllMyNotification(Integer brokerId) {
        MyUser broker = authRepository.findMyUserById(brokerId);

        if (broker == null) {
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }
        List<Notification> notifications = notificationRepository.findNotificationByMyUserId(brokerId);

        if (notifications.isEmpty()) {
            throw new ApiException("No Notifications found for the broker: " + brokerId);
        }

        return notifications;
    }

      //// ///////mark Notification to Done reading
       public void markNotification(Integer notificationId,Integer brokerId){
        CustomsBroker broker=customBrokerRepository.findCustomsBrokerById(brokerId);
        if(broker==null){
            throw new ApiException("Broker with ID " + brokerId + " not found.");
        }
        Notification notification=notificationRepository.findNotificationById(notificationId);
        if(notification==null){
            throw new ApiException("Notification with ID " + notificationId + " not found.");
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);
       }
}
