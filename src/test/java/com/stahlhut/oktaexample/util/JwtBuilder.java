package com.stahlhut.oktaexample.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Getter
@Setter
public class JwtBuilder {

    @Value("${okta.oauth2.issuer}")
    private static String issuer;

    private static Date dateNow = new Date(System.currentTimeMillis());
    private static Date dateExp = new Date(System.currentTimeMillis() + 8200000L);

    public static io.jsonwebtoken.JwtBuilder buildExpiredJwt(String jwtSubject, String claimName, String claimValue){
        dateExp = dateNow;
        return buildJwt(jwtSubject, claimName, claimValue);
    }


    public static io.jsonwebtoken.JwtBuilder buildJwt(String jwtSubject, HashMap<String, Object> claims){
        io.jsonwebtoken.JwtBuilder jwtsBuilder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setExpiration(dateExp)
                .setIssuedAt(dateNow)
                .setIssuer(issuer)
                .setSubject(jwtSubject);

        claims.forEach(jwtsBuilder::claim);


        return jwtsBuilder;
    }

    public static String signJwt(io.jsonwebtoken.JwtBuilder jwtsBuilder){

        String jwt = jwtsBuilder
                .signWith(SignatureAlgorithm.RS256, JwtSigningKeyPairSingleton.getInstance().getKeyPair().getPrivate())
                .compact();
        log.info("Created JWT with claims {}", jwtsBuilder.toString());
        return jwt;
    }

    public static io.jsonwebtoken.JwtBuilder buildJwt(String jwtSubject, String claimName, String claimValue){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(claimName, claimValue);
        return buildJwt(jwtSubject, claims);
    }

}
