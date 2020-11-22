package com.stahlhut.oktaexample.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;


public interface AuthorizationService {

    /**
     Checks if the user that's held in the SecurityContext has the authority "requiredRole"
     @param requiredRole e.g. ROLE_ADMIN
     @return true if authorized, throws Exception if not
     @throws AccessDeniedException if unauthorized
     */
    Boolean authorize(String requiredRole);


    /**
     Only used if JWT present. Checks if the user is authorized to access the given resourceIdentifier by
     comparing the value of the given claimName in the JWT to the resourceIdentifier
     @param claimName e.g. custId
     @param requiredRole e.g. ROLE_ADMIN
     @param resourceIdentifier e.g. 423342 (customer no.)
     @return true if authorized, throws Exception if not
     @throws AccessDeniedException if unauthorized
     */
    Boolean authorize(String requiredRole, String claimName, String resourceIdentifier);


}
