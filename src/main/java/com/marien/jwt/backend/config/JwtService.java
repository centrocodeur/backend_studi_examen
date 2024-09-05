package com.marien.jwt.backend.config;



import com.marien.jwt.backend.entities.Jwt;
import com.marien.jwt.backend.entities.User;
import com.marien.jwt.backend.repositories.JwtRepository;
import com.marien.jwt.backend.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
@AllArgsConstructor
@Service
public class JwtService {

    private UserService userService;
    private JwtRepository jwtRepository;




    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";

     public Jwt tokenByValue(String value){
        return this.jwtRepository.findByValueAndDeactivateAndExpire(
                value,
                false,
                false
                )
                .orElseThrow(()-> new RuntimeException("Token inconnu"));
    }


    public Map<String, String> generateToken(String username){
        User user = this.userService.loadUserByUsername(username);

        this.disableToken(user);
        final Map <String, String> jwtMap = this.createTokenJwt(user);
        final Jwt jwt= Jwt
                .builder()
                .value(jwtMap.get("bearer"))
                .deactivate(false)
                .expire(false)
                .user(user)
                .build();

        this.jwtRepository.save(jwt);

        return jwtMap;

    }

    private void  disableToken(User user){
        final List<Jwt> jwtList = this.jwtRepository.findByUser(user.getEmail()).peek(
                jwt -> {
                    jwt.setDeactivate(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());

        this.jwtRepository.saveAll(jwtList);
    }




    private Map<String, String> createTokenJwt(User user){
        Date now = new Date();
        Date validity = new Date(now.getTime()+ 3_600_000);
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime+30*60*1000;

        final Map<String, ? extends Serializable> claims= Map.of(
                "lastName", user.getLastName(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getEmail()

        );


        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("bearer", bearer);


    }

    private Key getKey(){
        final byte[] decoder= Decoders.BASE64.decode(ENCRIPTION_KEY); // ENCRIPTION_KEY
        return Keys.hmacShaKeyFor(decoder);
    }


    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims= getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public void deconnexion() {
       User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Jwt jwt= this.jwtRepository.findByUserValidToken(
                       user.getEmail(),
                      false,
                      false)
              .orElseThrow(()-> new RuntimeException("Token invalide"));
      jwt.setDeactivate(true);
      jwt.setExpire(true);
      this.jwtRepository.save(jwt);

    }





    @Scheduled(cron = "@daily")
    //@Scheduled(cron = "0 */1 * * * *")
    public void removeUselessJwt(){
        log.info("Jwt token remove at {}", Instant.now());
        this.jwtRepository.deleteAllByExpireAndDeactivate(true, true);
    }





}
