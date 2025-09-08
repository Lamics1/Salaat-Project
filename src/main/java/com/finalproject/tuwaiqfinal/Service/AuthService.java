package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String login(String username, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        User user = (User) authentication.getPrincipal();
        user.setLastLogin(LocalDateTime.now());
        authRepository.save(user);
        return jwtService.generateToken(user);
    }
}
