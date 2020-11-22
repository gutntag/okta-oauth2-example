package com.stahlhut.oktaexample;

import com.stahlhut.oktaexample.configuration.NimbusJwtDecoderConfig;
import com.stahlhut.oktaexample.util.JwtBuilder;
import com.stahlhut.oktaexample.web.HelloWorldController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Import({NimbusJwtDecoderConfig.class})
public class AuthorizationITest {
    @Autowired
    private MockMvc mockMvc;


    private static String jwtCustomer;
    private static String jwtCustomerInvalidClaim;
    private static String jwtAdmin;
    private static String jwtCustomerExpired;
    private static String protectedResource = "67896789";

    @BeforeAll
    static void setUp(){
        jwtCustomer = JwtBuilder.signJwt(JwtBuilder.buildJwt("random-email@example.com", "protectedresource", protectedResource).addClaims(buildRole("ROLE_CUSTOMER")));
        jwtCustomerInvalidClaim = JwtBuilder.signJwt(JwtBuilder.buildJwt("random-email@example.com", "protectedresource", "5435354").addClaims(buildRole("ROLE_CUSTOMER")));
        jwtAdmin = JwtBuilder.signJwt(JwtBuilder.buildJwt("random-email@example.com", buildRole("ROLE_ADMIN")));
        jwtCustomerExpired = JwtBuilder.signJwt(JwtBuilder.buildExpiredJwt("random-email@example.com", "testClaim", "testValue"));
    }

    private static HashMap buildRole(String role){
        HashMap<String, Collection> claims = new HashMap<>();
        claims.put("roles", Arrays.asList(role));
        return claims;
    }

    private static HashMap buildClaim(String key, String value){
        HashMap<String, String> claims = new HashMap<>();
        claims.put(key, value);
        return claims;
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

    @Test
    void testHelloWorldProtectedAllSuccessfully() throws Exception {
        mockMvc.perform(
                get(
                        linkTo(methodOn(HelloWorldController.class).getProtectedList()).toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtAdmin)
        )
                .andExpect(ResultMatcher.matchAll(status().isOk()));
    }

    @Test
    void testHelloWorldProtectedAllUnAuthorized() throws Exception {
        mockMvc.perform(
                get(
                        linkTo(methodOn(HelloWorldController.class).getProtectedList()).toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtCustomer)
        )
                .andExpect(ResultMatcher.matchAll(status().isForbidden()));
    }

    @Test
    void testHelloWorldProtectedSuccessfully() throws Exception {
        mockMvc.perform(
                get(
                        linkTo(methodOn(HelloWorldController.class).getProtectedResource(protectedResource)).toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtCustomer)
        )
                .andExpect(ResultMatcher.matchAll(status().isOk()));
    }
    @Test
    void testHelloWorldProtectedInvalidClaim() throws Exception {
        mockMvc.perform(
                get(
                        linkTo(methodOn(HelloWorldController.class).getProtectedResource(protectedResource)).toUri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtCustomerInvalidClaim)
        )
                .andExpect(ResultMatcher.matchAll(status().isForbidden()));
    }




}
