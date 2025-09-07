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
                .requestMatchers("/api/v1/payments/callback").permitAll()
                .requestMatchers("/api/v1/review-hall/getAll").permitAll()


                .requestMatchers("/api/v1/hall/get", "/api/v1/hall/get/{hallId}", "/api/v1/hall/get-subhalls/{hallId}").hasAnyAuthority("ADMIN","CUSTOMER","OWNER")
                .requestMatchers("/api/v1/game/get").hasAnyAuthority("ADMIN","CUSTOMER","OWNER")    //todo: get dto

                // Customer Authority
                .requestMatchers("api/v1/customer/get").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/get").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/add/subhall/{subhallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/update/booking/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/booking/delete/booking/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/get").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/update").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/delete").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/analyse").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/customer/cancel/booking/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-hall/add/{hallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-hall/update/{hallId}/{reviewHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-hall/delete/{hallId}/{reviewHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-sub-hall/add/{subHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-sub-hall/update/{subHallId}/{reviewSubHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/review-sub-hall/delete/{subHallId}/{reviewSubHallId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/pay/for/{bookingId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/card").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/get/status/{statusId}").hasAuthority("CUSTOMER")
                .requestMatchers("/api/v1/payments/download/invoice/{bookingId}").hasAuthority("CUSTOMER")

                // Owner Authority
                .requestMatchers("/api/v1/hall/get/my").hasAuthority("OWNER")
                .requestMatchers("/api/v1/booking/initiated/hall/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/booking/approved/hall/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/booking/remind-unpaid/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/game/add/{hallId}/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/game/update/{hallId}/{subHallId}/{gameId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/game/delete/{hallId}/{subHallId}/{gameId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/hall/add").hasAuthority("OWNER")  //todo: get dto
                .requestMatchers("/api/v1/hall/update/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/hall/delete/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/update").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/delete").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/get").hasAuthority("OWNER") //todo: create a dto to hide payments
                .requestMatchers("/api/v1/owner/feedback/hall/{hallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/feedback/subhall/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/owner/cancel/booking/{bookingId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/add/{hallId}").hasAuthority("OWNER")//todo: get dto
                .requestMatchers("/api/v1/subhall/update/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/delete/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/get/{subHallId}").hasAuthority("OWNER")
                .requestMatchers("/api/v1/subhall/hall/{hallId}/subhall/{subHallId}/budget/{pricePerHour}").hasAuthority("OWNER")

                // Admin Authority
                .requestMatchers("/api/v1/owner/get-all").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/review-hall/get").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/review-hall/hall/{hallId}/rating").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/review-sub-hall/get").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/customer/getall").hasAuthority("ADMIN")


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
