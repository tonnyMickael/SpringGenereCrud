package com.dev.template_crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.template_crud.model.Token;
import com.dev.template_crud.repository.TokenRepository;

import jakarta.servlet.http.Cookie;


@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public Token getTokenFromTokenName(String name ){
        return this.tokenRepository.findTokenByTokenName(name);
    }

    public boolean verifyCoockieUser(String coockie){
        try {
            if(coockie.isEmpty() || coockie == null){
                // false
                return false;
            }
            else {
                Token tokenFromDB = this.tokenRepository.findTokenByTokenName(coockie);
                if(tokenFromDB.getToken().equals(coockie)){
                    // true
                    return true;
                }
                else{
                    // false
                    return false;
                }
    
            }   
        } catch (Exception e) {
            e.getCause();
            return false;
        }
    }
}
