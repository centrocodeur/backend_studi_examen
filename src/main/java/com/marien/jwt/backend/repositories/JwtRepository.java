package com.marien.jwt.backend.repositories;

import com.marien.jwt.backend.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt, Integer> {


  Optional <Jwt> findByValueAndDeactivateAndExpire(String value, boolean deactivate, boolean expire);



  @Query("FROM Jwt j WHERE j.expire=:expire AND j.deactivate=:deactivate  AND j.user.email=:email")
  Optional<Jwt> findByUserValidToken(String email, boolean deactivate, boolean expire);


  @Query("FROM Jwt j WHERE j.user.email=:email")
 Stream <Jwt> findByUser(String email);


    void deleteAllByExpireAndDeactivate(boolean expire, boolean deactivate);



}
