package com.example.bayan.Repostiry;

import com.example.bayan.Model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental,Integer> {

    Rental findRentalById(Integer id);
   List<Rental> findRentalByBrokerId(Integer rentalID);

   Rental findRentalByCustomerIdAndBrokerId(Integer customerId, Integer brokerID);

   Rental findRentalByCustomerIdAndAndBrokerIdAndStatus(Integer customerId,Integer brokerId,String status);
   List<Rental> findRentalByCustomerId(Integer customerID);

   @Query("SELECT r FROM Rental r " +
           "WHERE r.customer.id = :customerId " +
           "AND r.status NOT IN ('Completed', 'Cancelled')")
   List<Rental> findNonCompletedNonCancelledRentalsByCustomerId(@Param("customerId")
                                                                Integer customerId);

   @Query("SELECT r FROM Rental r " +
           "WHERE r.broker.id = :brokerId " +
           "AND r.status NOT IN ('Completed', 'Cancelled')")
   List<Rental> findNonCompletedNonCancelledRentalsByBrokerId(@Param("brokerId") Integer brokerId);

}
