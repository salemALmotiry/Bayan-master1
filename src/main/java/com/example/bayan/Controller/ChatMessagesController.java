package com.example.bayan.Controller;

import com.example.bayan.Api.ApiResponse;
import com.example.bayan.DTO.ChatMessageDTO;
import com.example.bayan.Model.ChatMessages;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Service.ChatMessagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bayan/chat")
@RequiredArgsConstructor
public class ChatMessagesController {

    private final ChatMessagesService chatMessagesService;

    @PostMapping("/customer/send/{orderId}/{brokerId}")
    public ResponseEntity<?> sendMessageFromCustomer(@AuthenticationPrincipal MyUser customer,
                                                     @PathVariable Integer orderId,
                                                     @PathVariable Integer brokerId,
                                                     @RequestParam String messageText) {
        ChatMessages chatMessage = chatMessagesService.sendMessageFromCustomer(orderId, customer.getId(), brokerId, messageText);

        return ResponseEntity.status(200).body(new ApiResponse("Message sent successfully."));
    }

    @PostMapping("/broker/send/{orderId}/{customerId}")
    public ResponseEntity<?> sendMessageFromBroker(@AuthenticationPrincipal MyUser broker,
                                                   @PathVariable Integer orderId,
                                                   @PathVariable Integer customerId,
                                                   @RequestParam String messageText) {
        ChatMessages chatMessage = chatMessagesService.sendMessageFromBroker(orderId, broker.getId(), customerId, messageText);
        return ResponseEntity.status(200).body(new ApiResponse("Message sent successfully."));
    }

    @GetMapping("/customer/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessagesForCustomer(@AuthenticationPrincipal MyUser customer) {
        List<ChatMessageDTO> messages = chatMessagesService.getMessagesForCustomer(customer.getId());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/broker/messages")
    public ResponseEntity<List<ChatMessageDTO>> getMessagesForBroker(@AuthenticationPrincipal MyUser broker) {
        List<ChatMessageDTO> messages = chatMessagesService.getMessagesForBroker(broker.getId());
        return ResponseEntity.ok(messages);
    }



}
