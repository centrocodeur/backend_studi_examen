package com.marien.jwt.backend.services;


import com.marien.jwt.backend.dto.CredentialsDto;
import com.marien.jwt.backend.dto.SignUpDto;
import com.marien.jwt.backend.dto.UserDto;
import com.marien.jwt.backend.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserDto login(CredentialsDto credentialsDto);

    UserDto register(SignUpDto signUpDto);


    void updateUserById(User user, Long id);

    void register2(User user);


    User loadUserByUsername(String username);


}
