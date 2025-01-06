package com.example.bayan.Repostiry;

import com.example.bayan.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {

    List<Address> findAddressesByCustomerId(Integer customerId);
    Address findAddressById(Integer addressId);
    Address findAddressByCustomerId(Integer customerId);
}
