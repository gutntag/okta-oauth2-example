package com.stahlhut.oktaexample.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationServiceImplOktaJwt implements AuthorizationService{


    @Override
    public Boolean authorize(String requiredRole) {
        return authorizeRole(requiredRole);
    }

    @Override
    public Boolean authorize(String requiredRole,String claimName,String resourceIdentifier) {
        return authorizeRoleAndClaims(requiredRole, claimName, resourceIdentifier);
    }

    private Boolean authorizeRoleAndClaims(String requiredRole,String claimName,String resourceIdentifier){
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        //Admin is always allowed
        if(auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals(requiredRole))) {
            throw new AccessDeniedException("ROLE not allowed");
        }
        else {
            Jwt jwt = (Jwt) auth.getCredentials();
            if(! jwt.containsClaim(claimName)) {
                throw new AccessDeniedException("Claim missing!");
            }
            else {
                if(! jwt.getClaim(claimName).equals(resourceIdentifier)) {
                    throw new AccessDeniedException("Resource not allowed");
                }
            }
        }
        return true;
    }

    private Boolean authorizeRole(String requiredRole){
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().noneMatch(x -> x.getAuthority().equals(requiredRole))) {
            throw new AccessDeniedException("ROLE not allowed");
        }
        return true;
    }
}
