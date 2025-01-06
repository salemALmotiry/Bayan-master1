package com.example.bayan.Repostiry;

import com.example.bayan.Model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Integer> {

    Delivery findDeliveryById(Integer id);
    Delivery findDeliveryByIdAndOrderId(Integer id, Integer OrderId);


}
