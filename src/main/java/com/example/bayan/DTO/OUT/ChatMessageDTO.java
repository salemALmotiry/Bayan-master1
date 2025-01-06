
// DTO Class
package com.example.bayan.DTO;

import com.example.bayan.Model.ChatMessages;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageDTO {
    private Integer id;
    private String messageText;
    private String senderName;
    private String receiverName;
    private LocalDateTime sentAt;

    public static ChatMessageDTO fromEntity(ChatMessages chatMessage) {
        return new ChatMessageDTO(
                chatMessage.getId(),
                chatMessage.getMessageText(),
                chatMessage.getSender().getUsername(),
                chatMessage.getReceiver().getUsername(),
                LocalDateTime.now() // Replace with actual timestamp if available
        );
    }
}