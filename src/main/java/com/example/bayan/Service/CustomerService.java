package com.example.bayan.Service;

import com.example.bayan.Api.ApiException;
import com.example.bayan.DTO.IN.CbmDTO;
import com.example.bayan.DTO.IN.CustomerDTO;
import com.example.bayan.DTO.IN.UpdateCustomerDTO;
import com.example.bayan.DTO.OUT.CbmResponseDTO;
import com.example.bayan.Model.Customer;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Notification;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.CustomerRepository;
import com.example.bayan.Repostiry.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final NotificationRepository notificationRepository;


    public com.example.bayan.DTO.OUT.CustomerDTO getMyAccount(Integer userId){
            MyUser myUser=authRepository.findMyUserById(userId);

            if(myUser==null){
                throw new ApiException("Customer with this"+userId+" does not exist");

            }
            Customer customer=customerRepository.findCustomerById(userId);

            return new com.example.bayan.DTO.OUT.CustomerDTO(myUser.getUsername(), myUser.getFullName(), myUser.getEmail(),
                    myUser.getPhoneNumber(), customer.getCompanyName());
            }

    public void registerCustomer(CustomerDTO customerDTO){
        MyUser myUser=authRepository.findMyUserByUsername(customerDTO.getUsername());
        if(myUser!=null){
            throw new ApiException("User already exists");
        }

        MyUser myUser1=new MyUser();

        myUser1.setUsername(customerDTO.getUsername());

        myUser1.setPassword(new BCryptPasswordEncoder().encode(customerDTO.getPassword()));

        myUser1.setEmail(customerDTO.getEmail());

        myUser1.setPhoneNumber(customerDTO.getPhoneNumber());

        myUser1.setFullName(customerDTO.getFullName());

        myUser1.setRole("CUSTOMER");

        authRepository.save(myUser1);


        Customer customer=new Customer();
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUser(myUser1);

        customerRepository.save(customer);



    }

    public void updateCustomerAccount(Integer customerID, UpdateCustomerDTO customerDTO){
        MyUser oldCustomer=authRepository.findMyUserById(customerID);
        if(oldCustomer==null){
            throw new ApiException(" Customer id is wrong");
        }

        oldCustomer.setUsername(customerDTO.getUsername());
        oldCustomer.setFullName(customerDTO.getFullName());
        oldCustomer.setEmail(customerDTO.getEmail());
        oldCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
        oldCustomer.setUpdatedAt(LocalDateTime.now());

        authRepository.save(oldCustomer);

        Customer customer= oldCustomer.getCustomer();
        customer.setCompanyName(customerDTO.getCompanyName());

        customerRepository.save(customer);
    }


    // cbm
    public CbmResponseDTO calculateCbm(CbmDTO cbmDTO) {
        double length = cbmDTO.getLength();
        double width = cbmDTO.getWidth();
        double height = cbmDTO.getHeight();
        int quantity = cbmDTO.getQuantity();
        String unit = cbmDTO.getUnit();

        // Convert units to meters if necessary
        if ("in".equalsIgnoreCase(unit)) {
            length /= 39.37; // Convert inches to meters
            width /= 39.37;
            height /= 39.37;
        } else if ("cm".equalsIgnoreCase(unit)) {
            length /= 100; // Convert centimeters to meters
            width /= 100;
            height /= 100;
        }

        // Calculate total CBM
        double cbm = length * width * height * quantity;

        // Return a detailed response
        return new CbmResponseDTO(cbmDTO.getLength(), cbmDTO.getWidth(), cbmDTO.getHeight(), cbmDTO.getQuantity(), cbm);
    }

/////////////customer get All Notification

    public List<Notification>getAllMyNotification(Integer customerId){
        MyUser customer=authRepository.findMyUserById(customerId);

        if (customer==null)
            throw new ApiException("Customer with this"+customerId+" does not exist");

        List<Notification>notifications=notificationRepository.findNotificationByMyUserId(customerId);


        if(notifications==null)
            throw new ApiException("Customer with this"+customerId+" does not have any notifications");

           return notifications;

         }
          ///////mark Notification to Done reading
    public void markNotification(Integer notificationId,Integer customerId){
          Customer customer=customerRepository.findCustomerById(customerId);

             if (customer==null){
                 throw new ApiException("Customer with this"+customerId+" does not exist");
             }

           Notification notification=notificationRepository.findNotificationById(notificationId);
           if (notification==null){
               throw new ApiException("Notification with this"+notificationId+" does not exist");
           }
           notification.setIsRead(true);
           notificationRepository.save(notification);
         }
}
