package com.cms.incident.auth;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtil {

	public static void getInfo() {
		
//		System.out.println(".................");
//
//		Principal principal = SecurityContextHolder.getContext().getAuthentication();
//	 	
//		if (principal instanceof UserDetails) {
//	 		String username = ((UserDetails)principal). getUsername();
//	 		System.out.println(" can cast to UserDetails : " + username);
//	 	} else {
//	 		String username = principal. toString();
//	 		System.out.println(" CANNOT cast to UserDetails : " + username);
//
//	 	}
//	 	
	 	
		//  KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();        
		  KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) 
		  SecurityContextHolder.getContext().getAuthentication();
		  
		  KeycloakPrincipal kPrincipal = (KeycloakPrincipal)token.getPrincipal();
		  
		  	
	        KeycloakSecurityContext session = kPrincipal.getKeycloakSecurityContext();
	        
	        AccessToken accessToken = session.getToken();
	        String username = accessToken.getPreferredUsername();
	        String emailID = accessToken.getEmail();
	        String lastname = accessToken.getFamilyName();
	        String firstname = accessToken.getGivenName();
	        String  realmName = accessToken.getIssuer();            
	        Access realmAccess = accessToken.getRealmAccess();
	        Set<String> roles = realmAccess.getRoles();

			System.out.println(" username  : " + username);	
			System.out.println(" emailID  : " + emailID);
			System.out.println(" lastname  : " + lastname);
			System.out.println(" firstname  : " + firstname);
			System.out.println(" realmName  : " + realmName);
			System.out.println(" roles  : " + roles);

					 
		// custom attributes
			 Map<String, Object> customClaims  =	accessToken.getOtherClaims();
			 System.out.println("customClaims = " + customClaims);
			 if (customClaims.containsKey("workspace")) {
				 String wp = String.valueOf(customClaims.get("workspace"));
				 System.out.println(" custom wrokspace = " + wp);
			 }
	
	}
	
}
