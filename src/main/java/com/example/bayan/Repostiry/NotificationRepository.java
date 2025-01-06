package com.example.bayan.Repostiry;

import com.example.bayan.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {

   List<Notification>findNotificationByMyUserId(Integer myUser);

Notification findNotificationById(Integer notificationId);


}
