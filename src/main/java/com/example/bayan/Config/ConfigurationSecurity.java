package com.example.bayan.Config;


import com.example.bayan.Service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class ConfigurationSecurity {

    private final MyUserDetailsService myUserDeletesService;

    public ConfigurationSecurity(MyUserDetailsService myUserDeletesService) {
        this.myUserDeletesService = myUserDeletesService;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDeletesService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;

    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable() // Disable CSRF for testing purposes
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/bayan/custom-broker/register",
                                "/api/v1/bayan/customer/register",
                                "/api/v1/bayan/customer/calculate-cbm",
                                "api/v1/bayan/custom-broker/display-all-custom-brokers",

                        "/api/v1/bayan/custom-broker/license-number/{licenseNumber}",
                        "/api/v1/bayan/custom-broker/name/{name}",
                        "/api/v1/bayan/custom-broker/license-type/{type}" ,
                        "/api/v1/bayan/custom-broker/border/{border}",

                        "/api/v1/bayan/post/get-all-posts",
                        "/api/v1/bayan/subscription-post/get-all-subscription-posts",

                        "api/v1/bayan/post/by-category/{category}",
                        "api/v1/bayan/post/by-country/{countryOfOrigin}",
                        "api/v1/bayan/post/by-shipment-type/{shipmentType}",
                        "api/v1/bayan/post//by-category-and-country/{category}/{countryOfOrigin}",
                        "api/v1/bayan/post/by-category-and-shipment-type/{category}/{shipmentType}",
                        "api/v1/bayan/post/by-category-shipment-type-country/{category}/{shipmentType}/{countryOfOrigin}",

                        "/api/v1/bayan/review/broker-reviews/{brokerId}",
                        "/api/v1/bayan/delivery/track-air-shipment",
                        "/api/v1/bayan/delivery/track-sea-container",
                        "/api/v1/bayan/review/customer/{customerId}/reviews",
                        "/api/v1/bayan/review/customer/{customerId}/average-rating"
                ).permitAll()

                .requestMatchers(   "/api/v1/bayan/address/add-address",
                        "/api/v1/bayan/address/my-addresses",
                        "/api/v1/bayan/address/update-address/address-id/{address_id}",
                        "/api/v1/bayan/address/delete-address/address-id/{address_id}",


                        "/api/v1/bayan/chat/customer/messages",
                        "/api/v1/bayan/chat/customer/send/{orderId}/{brokerId}",

                        "/api/v1/bayan/customer/update-my-account",
                        "/api/v1/bayan/customer/my-account",

                        "/api/v1/bayan/customer/get-all-my-notifications",
                        "/api/v1/bayan/customer/read-my-notifications/{notificationId}/mark-as-read",

                        "/api/v1/bayan/offer/all-offer-post/{postId}",
                        "/api/v1/bayan/offer/accept-offer/{offerId}",
                        "/api/v1/bayan/offer/all-offer-sub-post/{postId}",
                        "/api/v1/bayan/offer/accept-offer-for-subscription-post/{offerId}",

                        "/api/v1/bayan/order/cancel-order/{orderId}",
                        "/api/v1/bayan/order/set-payment-waiting-for-approve/{orderId}",
                        "/api/v1/bayan/order/my-orders",
                        "/api/v1/bayan/order/order-details/{orderId}"

                        ,"/api/v1/bayan/post/add"
                        ,"/api/v1/bayan/post/update/{postId}"
                        ,"/api/v1/bayan/post/my-posts",


                        "/api/v1/bayan/post/delete/{postId}"
                        ,"/api/v1/bayan/post/send-ad-to-broker/{brokerId}",
                        "/api/v1/bayan/post/offer-statistics/{postId}",
                        "/api/v1/bayan/post/general-post-statistics"

                        ,"/api/v1/bayan/rental/customer-rentals"
                        ,"/api/v1/bayan/rental/customer-with-rentals"

                        ,"/api/v1/bayan/required-documents/upload-multiple/{postId}"
                        ,"/api/v1/bayan/required-documents/download-for-customer/{postId}/{documentId}"
                        ,"/api/v1/bayan/required-documents/get-files/{postId}"

                        ,"/api/v1/bayan/review/customer-rate-broker/{orderId}"
                        ,"/api/v1/bayan/review/customer-update-review-broker/{reviewId}"

                        ,"/api/v1/bayan/subscription-post/create-subscription-post"
                        ,"/api/v1/bayan/subscription-post/update-subscription-post/{post_id}"
                        ,"/api/v1/bayan/subscription-post/delete-subscription-post/{postId}"
                        ,"/api/v1/bayan/subscription-post/my-posts"

                        ,"/api/v1/bayan/subscription-post/create-subscription-post-for-broker/{brokerId}",
                        "/api/v1/bayan/delivery/track-by-carrier/{deliveryId}/{orderId}"

                ).hasAuthority("CUSTOMER")

                .requestMatchers("/api/v1/bayan/chat/broker/messages",
                        "/api/v1/bayan/chat/broker/send/{orderId}/{customerId}",

                        "/api/v1/bayan/custom-broker/my-profile",
                        "/api/v1/bayan/custom-broker/update-my-account",
                        "api/v1/bayan/custom-broker/update-borders",
                        "api/v1/bayan/custom-broker/remove-border/{borderId}",

                        "/api/v1/bayan/custom-broker/offer-statistics",
                        "/api/v1/bayan/custom-broker/review-statistics",

                        "/api/v1/bayan/delivery/update-status/{deliveryId}",
                        "/api/v1/bayan/delivery/add-carrier/{orderId}",

                        "/api/v1/bayan/offer/create-offer",
                        "/api/v1/bayan/offer/update-offer/{offerId}",
                        "/api/v1/bayan/offer/delete-offer/{offerId}",
                        "/api/v1/bayan/offer/create-sub-offer",

                        "/api/v1/bayan/offer/update-sub-offer/{offerId}",
                        "/api/v1/bayan/offer/create-offer-with-delivery",
                        "/api/v1/bayan/offer/update-offer-with-delivery/{offerId}",
                        "/api/v1/bayan/offer/delete-offer-with-delivery/{offerId}",

                        "/api/v1/bayan/offer/create-sub-offer-with-delivery",
                        "/api/v1/bayan/offer/update-sub-offer-with-delivery/{offerId}",

                        "/api/v1/bayan/order/cancel-order-broker/{orderId}",
                        "/api/v1/bayan/order/update-order-status/{orderId}",
                        "/api/v1/bayan/order/set-payment-completed/{orderId}",


                        "/api/v1/bayan/post/broker/posts",

                        "/api/v1/bayan/rental/broker-rentals",
                        "/api/v1/bayan/rental/broker-with-rentals",

                        "/api/v1/bayan/required-documents/download/{offerId}/{documentId}",
                        "/api/v1/bayan/required-documents/get-files-broker/{postId}/{customerId}",

                        "/api/v1/bayan/review/broker-rate-customer/{orderId}",
                        "/api/v1/bayan/review/broker-update-review-customer/{reviewId}",

                        "/api/v1/bayan/subscription-post/posts-for-broker"


                        ,"/api/v1/bayan/custom-broker/get-all-my-notifications"
                        ,"/api/v1/bayan/custom-broker/read-my-notifications/{notificationId}/mark-as-read"

                ).hasAuthority("BROKER")

                .requestMatchers("/api/v1/bayan/auth/custom-brokers",
                        "/api/v1/bayan/auth/reject-custom-broker/custom-broker/{customerId}",
                        "/api/v1/bayan/auth/accept-custom-broker/custom-broker/{customerId}")
                .hasAuthority("ADMIN")

                .anyRequest().denyAll()
                .and()
                .logout()
                .logoutUrl("/api/v1/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
        return http.build();
    }



}