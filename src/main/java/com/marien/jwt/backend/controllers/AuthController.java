package com.marien.jwt.backend.controllers;


import com.marien.jwt.backend.config.JwtService;
import com.marien.jwt.backend.dto.CredentialsDto;
import com.marien.jwt.backend.dto.UserDto;
import com.marien.jwt.backend.entities.User;
import com.marien.jwt.backend.services.imp.UserServiceImpl;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
//@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private UserServiceImpl userService;

    private AuthenticationManager authenticationManager;

    private JwtService jwtService;



    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody CredentialsDto credentialsDto){
        UserDto user = userService.login(credentialsDto);
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentialsDto.username(), credentialsDto.password())
        );
        if(authentication.isAuthenticated()){
            jwtService.generateToken(credentialsDto.username());
        }


        return ResponseEntity.ok(user);
    }



    @PostMapping(path = "inscription")
    public void inscription(@RequestBody User user) {
        log.info("Inscription");
        this.userService.register2(user);
    }

    @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activation){
        this.userService.activation(activation);

    }


    @PostMapping(path = "update_password")
    public void updatePassword(@RequestBody Map<String, String> activation){
        this.userService.updatePassword(activation);

    }

    @PostMapping(path = "new_password")
    public void newPassword(@RequestBody Map<String, String> activation){
        this.userService.newPassword(activation);

    }


    @PostMapping(path = "deconnexion")
    public void deconnexion(){
        this.jwtService.deconnexion();
    }


    @PostMapping(path = "connexion")
    public Map<String, String> connexion(@RequestBody CredentialsDto credentialsDto){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentialsDto.username(), credentialsDto.password())
        );
        if(authentication.isAuthenticated()){
            return this.jwtService.generateToken(credentialsDto.username());
        }

        return Collections.emptyMap();
    }




}
