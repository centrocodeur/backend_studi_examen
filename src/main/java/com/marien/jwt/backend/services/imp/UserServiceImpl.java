package com.marien.jwt.backend.services.imp;


import com.marien.jwt.backend.dto.CredentialsDto;
import com.marien.jwt.backend.dto.SignUpDto;
import com.marien.jwt.backend.dto.UserDto;
import com.marien.jwt.backend.entities.Role;
import com.marien.jwt.backend.entities.User;
import com.marien.jwt.backend.entities.Validation;
import com.marien.jwt.backend.exceptions.AppException;
import com.marien.jwt.backend.mappers.UserMapper;
import com.marien.jwt.backend.models.RoleType;
import com.marien.jwt.backend.repositories.UserRepository;
import com.marien.jwt.backend.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
//@RequiredArgsConstructor

@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private ValidationServiceImpl validationService;

    public UserDto login(CredentialsDto credentialsDto){
        User user = userRepository.findByEmail(credentialsDto.username())
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                 user.getPassword())){
            return userMapper.toUserDto(user);

        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);

    }



    public UserDto register(SignUpDto signUpDto){
        Optional<User> oUser = userRepository.findByEmail(signUpDto.email());

        if(oUser.isPresent()){
            throw new AppException("Login already existe", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);

        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password())));



        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);


    }


    @Override
    public void updateUserById
            (User user, Long id) {
        this.userRepository.findById(id);

    }

    @Override

    public void register2(User user) {
        Optional<User> optionalUser = this.userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent() && user.isEnabled()) {
            throw  new RuntimeException(" User existe déjà");
        }
        if(optionalUser.isPresent())
        {

        }
        String pwdCrypt = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(pwdCrypt);

        Role roleUtilisateur = new Role();
        roleUtilisateur.setRoleType(RoleType.USER);
        user.setRole(roleUtilisateur);

       user = this.userRepository.save(user);
        this.validationService.recordUser(user);
    }


    public void activation(Map<String, String> activation){
        Instant activate  = Instant.now();
        Validation validation = this.validationService.readCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Code expired");
        }
        User userActivated = this.userRepository.findById(validation.getUser().getId())
                .orElseThrow(()->new RuntimeException("unknown user"));
        userActivated.setActivated(true);
        validation.setActivation(activate);
        this.userRepository.save(userActivated);

    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("unknown user"));
    }

    public void updatePassword(Map<String, String> parameters) {
        User user = this.loadUserByUsername(parameters.get("email"));
        this.validationService.recordUser(user);
    }

    public void newPassword(Map<String, String> parameters) {
        User user= this.loadUserByUsername(parameters.get("email"));
       final Validation validation= validationService.readCode(parameters.get("code"));

       if(validation.getUser().getEmail().equals(user.getEmail())){
           String pwdCrypt = this.passwordEncoder.encode(parameters.get("password"));
           user.setPassword(pwdCrypt);
           this.userRepository.save(user);
       }



    }


    /*
    @PostConstruct
    public void postConstruct(){

        User user = new User();

        Role adminRole = new Role();
        adminRole.setRoleType(RoleType.ADMIN);

        user.setRole(adminRole);
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setActivated(true);
        user.setFirstName("Admin");
        user.setLastName("Admin");

        userRepository.save(user);


    }

     */


}
