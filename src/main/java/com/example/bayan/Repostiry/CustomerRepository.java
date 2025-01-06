package com.example.bayan.Repostiry;

import com.example.bayan.Model.Customer;
import com.example.bayan.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    Customer findCustomerById(Integer id);
    Customer findCustomerByUser(MyUser myUser);

}
