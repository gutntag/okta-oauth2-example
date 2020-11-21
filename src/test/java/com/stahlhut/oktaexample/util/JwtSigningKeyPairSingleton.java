package com.stahlhut.oktaexample.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.*;

@Getter
@Slf4j
public class JwtSigningKeyPairSingleton {

    // static variable jwtSigningKeyPairSingleton of type JwtSigningKeyPairSingleton
    private static JwtSigningKeyPairSingleton jwtSigningKeyPairSingleton = null;
    KeyPair keyPair = null;


    // private constructor restricted to this class itself
    private JwtSigningKeyPairSingleton()
    {
        log.info("Generating KeyPair for JWT signing...");
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            log.info("KeyPair generated with algorithm {}", keyPair.getPrivate().getAlgorithm());
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // static method to create instance of Singleton class
    public static JwtSigningKeyPairSingleton getInstance()
    {
        if (jwtSigningKeyPairSingleton == null){
            jwtSigningKeyPairSingleton = new JwtSigningKeyPairSingleton();
        }
        return jwtSigningKeyPairSingleton;
    }


}
