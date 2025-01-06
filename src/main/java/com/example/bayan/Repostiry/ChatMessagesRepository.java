package com.example.bayan.Repostiry;

import com.example.bayan.Model.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Integer> {

    List<ChatMessages> findBySenderIdOrReceiverId(Integer sender_id, Integer receiver_id);
}
