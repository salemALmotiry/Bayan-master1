package com.example.bayan.Service;

import com.example.bayan.Model.ChatMessages;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Orders;
import com.example.bayan.Repostiry.ChatMessagesRepository;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.OrdersRepository;
import com.example.bayan.DTO.ChatMessageDTO;
import com.example.bayan.Repostiry.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessagesService {



    private final ChatMessagesRepository chatMessagesRepository;

    private final AuthRepository myUserRepository;


    private final com.example.bayan.Repostiry.OrdersRepository ordersRepository;



    public ChatMessages sendMessageFromCustomer(Integer orderId, Integer customerId, Integer brokerId, String messageText) {
        // Validate order existence and ensure it involves both customer and broker
        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        if (!order.getOffer().getPost().getCustomer().getId().equals(customerId) || !order.getOffer().getBroker().getId().equals(brokerId)) {
            throw new IllegalArgumentException("Customer or broker is not part of this order.");
        }

        // Validate sender and receiver existence
        MyUser customer = myUserRepository.findMyUserById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);

        }


        MyUser broker = myUserRepository.findMyUserById(brokerId);
        if (broker == null){
            throw new IllegalArgumentException("Broker not found with ID: " + brokerId);

        }

        // Create and save chat message
        ChatMessages chatMessage = new ChatMessages();
        chatMessage.setOrder(order);
        chatMessage.setSender(customer);
        chatMessage.setReceiver(broker);
        chatMessage.setMessageText(messageText);

        return chatMessagesRepository.save(chatMessage);
    }

    public ChatMessages sendMessageFromBroker(Integer orderId, Integer brokerId, Integer customerId, String messageText) {
        // Validate order existence and ensure it involves both customer and broker
        Orders order = ordersRepository.findOrdersById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        if (!order.getOffer().getPost().getCustomer().getId().equals(customerId) || !order.getOffer().getBroker().getId().equals(brokerId)) {
            throw new IllegalArgumentException("Customer or broker is not part of this order.");
        }

        // Validate sender and receiver existence
        MyUser customer = myUserRepository.findMyUserById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);

        }


        MyUser broker = myUserRepository.findMyUserById(brokerId);
        if (broker == null){
            throw new IllegalArgumentException("Broker not found with ID: " + brokerId);

        }

        // Create and save chat message
        ChatMessages chatMessage = new ChatMessages();
        chatMessage.setOrder(order);
        chatMessage.setSender(broker);
        chatMessage.setReceiver(customer);
        chatMessage.setMessageText(messageText);

        return chatMessagesRepository.save(chatMessage);
    }


    public List<ChatMessageDTO> getMessagesForCustomer(Integer customerId) {
        List<ChatMessages> messages = chatMessagesRepository.findBySenderIdOrReceiverId(customerId, customerId);
        return messages.stream().map(ChatMessageDTO::fromEntity).collect(Collectors.toList());
    }

    public List<ChatMessageDTO> getMessagesForBroker(Integer brokerId) {
        List<ChatMessages> messages = chatMessagesRepository.findBySenderIdOrReceiverId(brokerId, brokerId);
        return messages.stream().map(ChatMessageDTO::fromEntity).collect(Collectors.toList());
    }
}

