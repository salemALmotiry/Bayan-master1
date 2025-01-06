package com.example.bayan.Repostiry;


import com.example.bayan.Model.Orders;
import com.example.bayan.Model.Post;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {


    Orders findOrdersById(Integer id);

    Orders findOrdersByIdAndOffer_Post_CustomerId(Integer id, Integer offer_post_customer_id);

    Orders findOrdersByIdAndOffer_BrokerId(Integer id, Integer broker_id);

    List<Orders> findAllByOffer_Post_CustomerId(Integer userId);

    @Query("select o from Orders o where o.offer.post.customer.id=?1 and o.id=?2")
    Orders findOrdersByIdAndCustomerId(Integer customerId,Integer  orderId);

}
