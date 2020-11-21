package com.stahlhut.oktaexample;

import com.stahlhut.oktaexample.configuration.NimbusJwtDecoderConfig;
import com.stahlhut.oktaexample.util.JwtBuilder;
import com.stahlhut.oktaexample.web.HelloWorldController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import(NimbusJwtDecoderConfig.class)
public class AuthorizationITest {
    @Autowired
    private MockMvc mockMvc;

    private static String jwtCustomer;
    private static String jwtCustomerExpired;

    @BeforeAll
    static void setUp(){
        jwtCustomer = JwtBuilder.buildJwt("random-email@example.com", "testClaim", "testValue");
        jwtCustomerExpired = JwtBuilder.buildExpiredJwt("random-email@example.com", "testClaim", "testValue");
    }

    @Test
    void testHelloWorldAnonSuccessfully() throws Exception {
        mockMvc.perform(
            get(
                    linkTo(methodOn(HelloWorldController.class).getHelloWorldAnon()).toUri())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + jwtCustomer)
                )
                .andExpect(ResultMatcher.matchAll(status().isOk()));
    }

    @Test
    void testHelloWorldAnonInvalidAuthHeader() throws Exception {
        mockMvc.perform(
                get(
                        linkTo(methodOn(HelloWorldController.class).getHelloWorldAnon()).toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "" + jwtCustomer)
        )
                .andExpect(ResultMatcher.matchAll(status().isUnauthorized()));
    }

    @Test
    void testHelloWorldAnonExpiredJwt() throws Exception {
        mockMvc.perform(
                get(
                        linkTo(methodOn(HelloWorldController.class).getHelloWorldAnon()).toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtCustomerExpired)
        )
                .andExpect(ResultMatcher.matchAll(status().isUnauthorized()));
    }


}
