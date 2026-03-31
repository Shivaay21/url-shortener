package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.LoginRequestDTO;
import com.example.urlshortner.dto.request.RegisterRequestDTO;
import com.example.urlshortner.entity.User;
import com.example.urlshortner.exception.AliasAlreadyExistsException;
import com.example.urlshortner.exception.EmailAlreadyExistsException;
import com.example.urlshortner.exception.UnauthorizedException;
import com.example.urlshortner.exception.UserNotFoundException;
import com.example.urlshortner.repository.UserRepository;
import com.example.urlshortner.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO requestDTO){
        if(userRepository.findByEmail(requestDTO.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        userRepository.save(user);

        log.info("User registered: {}", requestDTO.getEmail());
        return "User Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO requestDTO){
        User existing = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(requestDTO.getPassword(), existing.getPassword())){
            throw new UnauthorizedException("Invalid credentials");
        }
        return jwtUtil.generateToken(existing.getEmail());
    }
}
