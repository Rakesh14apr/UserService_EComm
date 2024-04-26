package com.auth1.auth.learning.service;

import com.auth1.auth.learning.model.Token;
import com.auth1.auth.learning.model.User;
import com.auth1.auth.learning.repository.TokenRepository;
import com.auth1.auth.learning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User SignUp(String email, String password, String name){

        User user=new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        return userRepository.save(user);
    }

    public Token login(String email, String password) {

        Optional<User> userOptional=userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User Not Registered");
        }

        User user= userOptional.get();
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect User Name or Password");
        }

        Token token =new Token();

        token.setUser(user);
        token.setValue(UUID.randomUUID().toString());

       Date expiredDate=getExpiredDate();
        token.setExpireAt(expiredDate);

        return tokenRepository.save(token);
                //@55mins 15 secs in class
    }

    // expiration date will be +30 days to current date
    private Date getExpiredDate() {
        Calendar calenderDate = Calendar.getInstance();
        calenderDate.setTime(new Date());

        //add(Calender.DAY_OF_MONTH,-5).
        calenderDate.add(Calendar.DAY_OF_MONTH,30);

        Date expiredDate= calenderDate.getTime();

        return expiredDate;
    }

    public void logout(String token) {
        //check token status i.e., is token present or not
        //5	2024-05-26 22:57:40.248000	67a10907-e62f-41a4-81ef-b285c68d8d74	1
        Optional<Token> tokenOptional=tokenRepository.findByValueAndDeletedEquals(token,false);

        if (tokenOptional.isEmpty()) {
            //token deleted
            throw new RuntimeException("Expired Session");
        }

        Token tokenObject=tokenOptional.get();
        tokenObject.setDeleted(true);

        tokenRepository.save(tokenObject);
    }

    public boolean validateToken(String token) {
        /*
            To validate token
            1. check if token value is present
            2. check if token is not deleted
            3. check if token is not expired
        */

        Optional<Token> tokenOptional=tokenRepository.findByValueAndDeletedEqualsAndExpireAtGreaterThan(
            token,false,new Date());
        if (tokenOptional.isEmpty()) {
            return false;
        }
        return true;
    }
}
