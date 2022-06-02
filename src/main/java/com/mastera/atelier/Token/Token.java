package com.mastera.atelier.Token;

import com.mastera.atelier.Models.User;
import com.mastera.atelier.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;

public class Token {
    private final String SECRET_KEY = "ABCDEFJHIGKLMNOPQRSTUVWXYZ0123456789ATELIERMASTERA";

    public String tokenByUsername(String username){
        return Jwts
                .builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public String usernameByToken(String token){
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
