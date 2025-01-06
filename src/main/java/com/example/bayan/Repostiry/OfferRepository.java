package com.example.bayan.Repostiry;

import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Offer;
import com.example.bayan.Model.Post;
import com.example.bayan.Model.SubscriptionPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    Offer findOfferById(Integer id);

    Offer findOfferByPostIdAndBrokerId(Integer postId, Integer brokerId);

    Offer findOfferBySubscriptionPostIdAndBrokerId(Integer subscriptionId, Integer brokerId);

   Offer findOfferByIdAndPost_CustomerId(Integer id, Integer customerId);


    @Query("SELECT o FROM Offer o WHERE o.id = :offerId AND o.subscriptionPost.customer.user.id = :customerId")
    Offer findOfferByIdAndSupPost_CustomerId(@Param("offerId") Integer offerId, @Param("customerId") Integer customerId);

    Offer findOfferByIdAndPost_CustomerIdAndBrokerIdAndOfferStatus(Integer id, Integer post_customer_id, Integer broker_id, String offerStatus);

    @Query("SELECT o FROM Offer o WHERE o.id = :id AND o.post.customer.id = :postCustomerId AND o.broker.id = :brokerId AND o.offerStatus NOT IN ('Canceled', 'Completed','Pending','Rejected')")
    Offer findActiveOfferByIdAndPostCustomerIdAndBrokerId(@Param("id") Integer id, @Param("postCustomerId") Integer postCustomerId, @Param("brokerId") Integer brokerId);


    List<Offer> findAllByPostId(Integer postId);

    List<Offer> findAllBySubscriptionPostId(Integer subscriptionId);
    List<Offer> getAllOfferByPost(Post post);

    List<Offer>getAllOfferByPostAndPostCustomerId(Post post, Integer customerId);

    List<Offer> getAllOfferBySubscriptionPostAndSubscriptionPostCustomerId(SubscriptionPost subscriptionPost, Integer subscriptionPost_customer_id);

    Offer findOfferByIdAndBrokerId(Integer id, Integer broker_id);

    Boolean existsByPostAndBrokerAndOfferStatus(Post post, CustomsBroker broker, String offerStatus);

    Boolean existsBySubscriptionPostAndBrokerAndOfferStatus(SubscriptionPost subscriptionPost, CustomsBroker broker, String offerStatus);
}
