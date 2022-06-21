/*
 * PrincipalExtractorImpl
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.oauth;

import eu.profinit.stm.service.user.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is used to extract user information (principals) form authentication mainly by OAuth2 services,
 * currently google and facebook.
 */
@Component
public class PrincipalExtractorImpl implements PrincipalExtractor {
    @Autowired
    private AuthenticationFacade authenticationFacade;

    /**
     * Currently not in use.
     */
    @Override
    public Map<String, Object> extractPrincipal(Map<String, Object> map) {
        return map;
    }

    /**
     * Maps user details from authentication.
     * For each authentication service provides different maps,
     * however each map always contains "email" and "username" attribute.
     * For non-OAuth login, now mainly used in Tests, the "email" and "username" attributes are the same.
     *
     * @return map of user details
     */
    private Map<String, Object> getPrincipalMap() {
        Authentication authentication = authenticationFacade.getAuthentication();
        String className = String.valueOf(authentication.getPrincipal().getClass());
        //Google
        switch (className) {
        case "class org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser": {
            DefaultOidcUser userPrincipals = (DefaultOidcUser) authentication.getPrincipal();
            return userPrincipals.getAttributes();
        }
        //Facebook
        case "class org.springframework.security.oauth2.core.user.DefaultOAuth2User": {
            DefaultOAuth2User userPrincipals = (DefaultOAuth2User) authentication.getPrincipal();
            return userPrincipals.getAttributes();
        }
        //@WithMockUser
        case "class org.springframework.security.core.userdetails.User": {
            User userPrincipals = (User) authentication.getPrincipal();
            Map<String, Object> map = new HashMap<>();
            map.put("email", userPrincipals.getUsername());
            map.put("name", userPrincipals.getUsername());
            return map;
        }
        }
        return null;
    }


    /**
     * Returns email from map of user details of currently authenticated user.
     *
     * @return email of the authenticated user
     */
   public String getPrincipalEmail() {
        return (String) extractPrincipal(getPrincipalMap()).get("email");
   }

    /**
     * Gets the name and surname from the user details map attribute "name". Because the name is a full user name, e.g.
     * contains both given name and surname, the string needs to be split.
     * Till the last space character is taken as a given name (names) and
     * the rest of the string (last word) is taken as a surname.
     *
     * In the case of "@WithMockUser", the "name" attribute contains only email address and so
     * the email address is taken as and given name and surname is empty string.
     *
     * @return string array of user given name and surname
     */
   public String[] getPrincipalNameAndSurname() {
       String fullName = (String) extractPrincipal(getPrincipalMap()).get("name");
       int surnameFrom = fullName.lastIndexOf(" ");
       String name;
       String surname = "";
       if (surnameFrom > 0) {
           name = fullName.substring(0, surnameFrom);
           surname = fullName.substring(surnameFrom + 1);
       } else {
          name = fullName;
       }
       return new String[]{name, surname};
   }
}
