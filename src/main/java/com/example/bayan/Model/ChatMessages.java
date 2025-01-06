package com.example.bayan.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Lob
    private String messageText;



    // Which offer does this chat belong to?
    @ManyToOne
    @JsonIgnore
    private Orders order;

    // Who sent the message?
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private MyUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private MyUser receiver;


}
