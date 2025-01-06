package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.DeliveryDTO;
import com.example.bayan.Model.*;
import com.example.bayan.Repostiry.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final CustomBrokerRepository customBrokerRepository;
    private final DeliveryRepository deliveryRepository;
    private final AuthRepository authRepository;
    private final OrdersRepository ordersRepository;
    private final OfferRepository offerRepository;
    private final NotificationRepository notificationRepository;
    private final AddressRepository addressRepository;


    public void addCarrier(Integer brokerID, Integer orderId,DeliveryDTO deliveryDTO){
        MyUser broker=authRepository.findMyUserById(brokerID);

        if(broker == null){
            throw new ApiException("Broker with ID " + brokerID + " not found.");
        }
        Offer offer = offerRepository.findOfferByIdAndBrokerId(orderId,brokerID);

        if (offer == null) {
            throw new ApiException("Order with ID " + orderId + " not found.");
        }
        Orders order = ordersRepository.findOrdersById(orderId);

        if (!order.getStatus().equals("Completed")) {
            throw new ApiException("Order with ID " + orderId + " is not completed.");
        }

        Address address = addressRepository.findAddressByCustomerId(offer.getPost().getCustomer().getId());
        if (address == null) {
            throw new ApiException("Address with ID " + offer.getPost().getCustomer().getId() + " not found.");
        }
        Delivery delivery = new Delivery();

        delivery.setCarrier(deliveryDTO.getCarrier());
        delivery.setTrackingNumber(deliveryDTO.getTrackingNumber());

        delivery.setOrder(order);
        deliveryRepository.save(delivery);

        Notification notification = new Notification();
        notification.setMassage("تم إنشاء طلب توصيل بنجاح");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(broker);
    }


    public void updateStatus(Integer deliveryID, Integer brokerID) {
        // Fetch the broker
        MyUser broker = authRepository.findMyUserById(brokerID);
        if (broker == null)
            throw new ApiException("Broker with ID " + brokerID + " not found.");


        // Fetch the delivery
        Delivery oldDelivery = deliveryRepository.findDeliveryById(deliveryID);

        if (oldDelivery == null)
            throw new ApiException("Delivery with ID " + deliveryID + " not found.");

        Orders order = oldDelivery.getOrder();
        
        if (!order.getStatus().equals("Completed"))
            throw new ApiException("order with ID " + order.getId() + " not Completed.");

        if (oldDelivery.getCarrier()!=null){
            throw new ApiException("Carrier already exists.");
        }

        
        if (!Objects.equals(order.getOffer().getBroker().getId(), broker.getId()))
            throw new ApiException("Order with ID " + order.getId() + " is not .");

        
        // Determine the next status
        String currentStatus = oldDelivery.getStatus();
        String nextStatus = getNextStatus(currentStatus);

        if (nextStatus == null) {
            throw new ApiException("Delivery is already in the final status and cannot be updated further.");
        }

        // Update the delivery status
        oldDelivery.setStatus(nextStatus);
        deliveryRepository.save(oldDelivery);

        // Create a notification
        Notification notification = new Notification();
        notification.setMassage("تم تحديث حالة الشحنة إلى: " + nextStatus);
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(broker);

        // Save or send the notification (assumes a notification repository or service exists)
        notificationRepository.save(notification);
    }

    // Helper method to determine the next status
    private String getNextStatus(String currentStatus) {
        return switch (currentStatus) {
            case "STARTED" -> "IN_PROGRESS";
            case "IN_PROGRESS" -> "COMPLETED";
            case "COMPLETED" -> null; // Final status, no further transitions
            default -> "STARTED"; // Default to STARTED if status is null or unknown
        };
    }

    public Map<String, Object> trackAirShipment(String awbNumber) {
        // منطق للتتبع الجوي
        // استدعاء الـ Flask API
        String url = "https://63dd-51-39-60-37.ngrok-free.app/track-shipment";
        Map<String, String> payload = Map.of("awb_number", awbNumber);
        return makePostRequest(url, payload);
    }

    public Map<String, Object> trackSeaContainer(String containerNumber) {
        // منطق للتتبع البحري
        // استدعاء الـ Flask API
        String url = "https://63dd-51-39-60-37.ngrok-free.app/track-container";
        Map<String, String> payload = Map.of("container_number", containerNumber);
        return makePostRequest(url, payload);
    }

    public Map<String, Object> trackByCarrier(Integer customerId,Integer deliveryId , Integer orderId) {
        MyUser myUser = authRepository.findMyUserById(customerId);
        if (myUser == null){
            throw new ApiException("Customer with ID " + customerId + " not found.");
        }
        Orders order = ordersRepository.findOrdersByIdAndOffer_Post_CustomerId(orderId,customerId);
        if (order == null){
            throw new ApiException("Order with ID " + orderId + " not found.");
        }
        Delivery delivery = deliveryRepository.findDeliveryByIdAndOrderId(deliveryId,orderId);
        if (delivery == null){
            throw new ApiException("Delivery with ID " + deliveryId + " not found.");
        }
        if (delivery.getCarrier() == null){
            throw new ApiException("Carrier with ID " + deliveryId + " not found.");
        }
        String url = switch (delivery.getCarrier().toLowerCase()) {
            case "aramex" -> "https://63dd-51-39-60-37.ngrok-free.app/track-aramex";
            case "dhl" -> "https://63dd-51-39-60-37.ngrok-free.app/track-dhl";
            case "naqel" -> "https://63dd-51-39-60-37.ngrok-free.app/track-naqel";
            default -> throw new IllegalArgumentException("Unsupported carrier: " + delivery.getCarrier());
        };
        Map<String, String> payload = Map.of("tracking_number", delivery.getTrackingNumber());
        return makePostRequest(url, payload);
    }

    private Map<String, Object> makePostRequest(String url, Map<String, String> payload) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        return response.getBody();
    }
}
