package com.finalproject.tuwaiqfinal.Config;

import com.finalproject.tuwaiqfinal.Service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()
                // Permit All
                .requestMatchers("/api/v1/customer/register", "/api/v1/owner/register").permitAll()
                .requestMatchers("/api/v1/hall/get", "/api/v1/hall/get/{hallId}").permitAll()
                .requestMatchers("/api/v1/review-hall/get").permitAll()
                .requestMatchers("/api/v1/review-sub-hall/get").permitAll()
                .requestMatchers("/api/v1/hall/get-subhalls/{hallId}").permitAll()

                //hasAnyAuth
                .requestMatchers("/api/v1/payments/get/status/{statusId}").hasAnyAuthority("CUSTOMER","OWNER","ADMIN")

                // Customer Authority
                .requestMatchers("/api/v1/booking/get/customer/{customer_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/add/customer/{customer_id}/subhall/{subhall_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/update/customer/{customer_id}/booking/{booking_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/delete/customer/{customer_id}/booking/{booking_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/get/{customer_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/update/customer/{customer_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/delete/customer/{customer_id}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/cancel/by/{customerId}/booking/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-hall/add/{customerId}/{hallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-hall/update/**").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-hall/delete/{customerId}/{hallId}/{reviewHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-sub-hall/add/{customerId}/{subHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-sub-hall/update/{customerId}/{subHallId}/{ReviewSubHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-sub-hall/delete/**").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/pay/by/{customerId}/for/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/callback").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/analyse").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/card").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/download/invoice/{bookingId}").hasAuthority("CUSTOMER")

                // Owner Authority
                .requestMatchers("/api/v1/game/get").hasAuthority("OWNER")
                .requestMatchers("/api/v1/booking/initiated/owner/{ownerId}/hall/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/booking/Approved/owner/{ownerId}/hall/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/booking/remind-unpaid/{ownerId}/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/game/add/{OwnerId}/{hallId}/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/game/update/{OwnerId}/{hallId}/{subHallId}/{gameId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/game/delete/{OwnerId}/{hallId}/{subHallId}/{gameId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/hall/add/{ownerId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/hall/update/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/hall/delete/by/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/update/{ownerId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/delete/{ownerId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/feedback/to/{owner_id}/for/{hall_id}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/feedback/to/{owner_id}/for/subhall/{sub_hall_id}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/cancel/by/{ownerId}/booking/{bookingId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/add/by/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/update/by/**").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/delete/{ownerId}/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/get/{ownerId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/get/{subHallId}").hasAnyAuthority("OWNER")    //todo:dto
                .requestMatchers("/api/v1/subhall/hall/{hallId}/subhall/{subHallId}/budget/{pricePerHour}").hasAuthority("CUSTOMER")

                // Admin Authority
                .requestMatchers("/api/v1/owner/get").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/customer/get").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/review-hall/hall/{hallId}/rating/").hasAuthority("ADMIN")


















                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("api/v1/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();   //auth

        return httpSecurity.build();
    }

}
