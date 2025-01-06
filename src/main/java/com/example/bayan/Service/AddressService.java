package com.example.bayan.Service;


import com.example.bayan.Api.ApiException;
import com.example.bayan.Model.Address;
import com.example.bayan.Model.MyUser;
import com.example.bayan.Model.Notification;
import com.example.bayan.Repostiry.AddressRepository;
import com.example.bayan.Repostiry.AuthRepository;
import com.example.bayan.Repostiry.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AuthRepository authRepository;
    private final NotificationRepository notificationRepository;

    // add address by customer for delivery
    public void addDeliveryAddress(Integer customer_id,Address address){
        MyUser customer =  authRepository.findMyUserById(customer_id);
        if(customer==null){
            throw new ApiException("Wrong customer Id");
        }
        address.setCustomer(customer.getCustomer());
        addressRepository.save(address);
        authRepository.save(customer);//check

        ////notification for user when he add new address
        Notification notification=new Notification();
        notification.setMassage("تم اضافة العنوان بنجاح");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(customer);
        notificationRepository.save(notification);

    }

    // get myAddresses
    public List<com.example.bayan.DTO.OUT.AddressDTO> myAddresses(Integer customer_id) {
        MyUser customer = authRepository.findMyUserById(customer_id);

        if (customer == null) {
            throw new ApiException("Customer with ID " + customer_id + " does not exist");
        }

        List<Address> addresses = addressRepository.findAddressesByCustomerId(customer_id);

        return addresses.stream()
                .map(address -> new com.example.bayan.DTO.OUT.AddressDTO(
                        address.getCity(),
                        address.getNeighborhood(),
                        address.getStreet(),
                        address.getPostalCode(),
                        address.getBuildingNumber()
                ))
                .collect(Collectors.toList());
    }


    // update address
    public void updateAddress(Integer customer_id,Integer address_id, Address address) {
      MyUser customer = authRepository.findMyUserById(customer_id);
      if (customer == null) {
          throw new ApiException("Wrong customer Id");
      }
      Address oldAddress = addressRepository.findAddressById(address_id);
        if (oldAddress == null) {
            throw new ApiException("Wrong address Id");
        }
      if(!customer_id.equals(oldAddress.getCustomer().getId())){
          throw new ApiException("you are not authorized to update this address");
      }
      oldAddress.setCity((address.getCity()));
      oldAddress.setStreet(address.getStreet());
      oldAddress.setNeighborhood(address.getNeighborhood());
      oldAddress.setPostalCode(address.getPostalCode());
      oldAddress.setBuildingNumber(address.getBuildingNumber());
      addressRepository.save(oldAddress);

        ////notification for user when he update  address
        Notification notification=new Notification();
        notification.setMassage("تم تحديث العنوان بنجاح");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(customer);
        notificationRepository.save(notification);
    }

    // delete address
    public void deleteAddress(Integer customer_id, Integer address_id) {
        MyUser customer = authRepository.findMyUserById(customer_id);
        if (customer == null) {
            throw new ApiException("Wrong customer Id");
        }
        Address oldAddress = addressRepository.findAddressById(address_id);
        if (oldAddress == null) {
            throw new ApiException("Wrong address Id");
        }
        if(!customer_id.equals(oldAddress.getCustomer().getId())){
            throw new ApiException("you are not authorized to update this address");
        }
        customer.getCustomer().getAddresses().remove(oldAddress);
        authRepository.save(customer);
        addressRepository.delete(oldAddress);

        ////notification for user when he delet address
        Notification notification=new Notification();
        notification.setMassage("تم حذف العنوان بنجاح");
        notification.setCreateAt(LocalDateTime.now());
        notification.setMyUser(customer);
    }

}
