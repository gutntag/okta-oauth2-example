package com.stahlhut.oktaexample.configuration;

import com.stahlhut.oktaexample.util.JwtSigningKeyPairSingleton;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPublicKey;


@TestConfiguration
@Slf4j
@ConditionalOnClass({NimbusJwtDecoder.class})
public class NimbusJwtDecoderConfig {

    @Bean
    @ConditionalOnMissingBean
    public NimbusJwtDecoder configureNimbusJwtDecoder() {
        log.info("Configuring test NimbusJwtDecoder!");
        JwtSigningKeyPairSingleton jwtSigningKeyPair = JwtSigningKeyPairSingleton.getInstance();
        RSAPublicKey publicKey = (RSAPublicKey) jwtSigningKeyPair.getKeyPair().getPublic();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }


}
