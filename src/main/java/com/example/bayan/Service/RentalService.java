package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.OUT.BrokerRentalsDTO;
import com.example.bayan.DTO.OUT.CustomerDTO;
import com.example.bayan.DTO.OUT.CustomerRentalsDTO;
import com.example.bayan.DTO.OUT.RentalDTO;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.CustomsBroker;
import com.example.bayan.Model.Rental;
import com.example.bayan.Repostiry.CustomBrokerRepository;
import com.example.bayan.Repostiry.CustomerRepository;
import com.example.bayan.Repostiry.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CustomBrokerRepository customBrokerRepository;
    private  final CustomerRepository customerRepository;



    ///  CRUD - Start

    public List<RentalDTO> getCustomerRental(Integer customerId) {
        List<Rental> rentals = rentalRepository.findRentalByCustomerId(customerId);

        List<RentalDTO> rentalDTOS = new ArrayList<>();

        for (Rental rental : rentals) {
            RentalDTO rentalDTO = new RentalDTO();

            rentalDTO.setStartDateTime(rental.getStartDateTime());
            rentalDTO.setNumberOfOrder(rental.getNumberOfOrder());
            rentalDTO.setTotalPrice(rental.getTotalPrice());
            rentalDTO.setSupPrice(rental.getSupPrice());
            rentalDTO.setStatus(rental.getStatus());

            rentalDTOS.add(rentalDTO);
        }

        return rentalDTOS;
    }

    public List<RentalDTO> getBrokerRental(Integer brokerId) {
        List<Rental> rentals = rentalRepository.findRentalByBrokerId(brokerId);

        List<RentalDTO> rentalDTOS = new ArrayList<>();

        for (Rental rental : rentals) {
            RentalDTO rentalDTO = new RentalDTO();

            rentalDTO.setStartDateTime(rental.getStartDateTime());
            rentalDTO.setNumberOfOrder(rental.getNumberOfOrder());
            rentalDTO.setTotalPrice(rental.getTotalPrice());
            rentalDTO.setSupPrice(rental.getSupPrice());
            rentalDTO.setStatus(rental.getStatus());

            rentalDTOS.add(rentalDTO);
        }

        return rentalDTOS;
    }


    public CustomerRentalsDTO getCustomerWithRentals(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);
          if(customer==null){
         throw  new ApiException("Customer with ID " + customerId + " not found.");}

        CustomerRentalsDTO customerDTO = new CustomerRentalsDTO();
        customerDTO.setFullName(customer.getUser().getFullName());
        customerDTO.setEmail(customer.getUser().getEmail());
        customerDTO.setPhoneNumber(customer.getUser().getPhoneNumber());


        List<Rental> rentals = rentalRepository.findNonCompletedNonCancelledRentalsByCustomerId(customerId);

        List<RentalDTO> rentalDTOs = new ArrayList<>();
             for(Rental rental : rentals) {
                 RentalDTO rentalDTO = new RentalDTO();
                 rentalDTO.setStartDateTime(rental.getStartDateTime());
                 rentalDTO.setNumberOfOrder(rental.getNumberOfOrder());
                 rentalDTO.setTotalPrice(rental.getTotalPrice());
                 rentalDTO.setSupPrice(rental.getSupPrice());
                 rentalDTO.setStatus(rental.getStatus());

                 rentalDTOs.add(rentalDTO);
             }
         customerDTO.setRentalDTOS(rentalDTOs);
             return customerDTO;
    }


    public BrokerRentalsDTO getBrokerWithRentals(Integer brokerId) {

        CustomsBroker customsBroker=customBrokerRepository.findCustomsBrokerById(brokerId)
;
        if(customsBroker==null){
            throw  new ApiException("Customer with ID " + brokerId + " not found.");}

        BrokerRentalsDTO brokerDto = new BrokerRentalsDTO();
        brokerDto.setFullName(customsBroker.getUser().getFullName());
        brokerDto.setPhoneNumber(customsBroker.getUser().getPhoneNumber());
        brokerDto.setCompanyName(customsBroker.getCompanyName());


        List<Rental> rentals = rentalRepository.findNonCompletedNonCancelledRentalsByCustomerId(brokerId);

        List<RentalDTO> rentalDTOs = new ArrayList<>();
        for(Rental rental : rentals) {
            RentalDTO rentalDTO = new RentalDTO();
            rentalDTO.setStartDateTime(rental.getStartDateTime());
            rentalDTO.setNumberOfOrder(rental.getNumberOfOrder());
            rentalDTO.setTotalPrice(rental.getTotalPrice());
            rentalDTO.setSupPrice(rental.getSupPrice());
            rentalDTO.setStatus(rental.getStatus());

            rentalDTOs.add(rentalDTO);
        }
        brokerDto.setRentalDTOS(rentalDTOs);
        return brokerDto;
    }






}

