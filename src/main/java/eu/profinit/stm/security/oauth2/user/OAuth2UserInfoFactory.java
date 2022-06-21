package eu.profinit.stm.security.oauth2.user;

import eu.profinit.stm.model.user.SocialProviderEnum;
import eu.profinit.stm.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialProviderEnum socialProvider, Map<String, Object> attributes) {
        if (SocialProviderEnum.GOOGLE.equals(socialProvider)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (SocialProviderEnum.FACEBOOK.equals(socialProvider)) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (SocialProviderEnum.GITHUB.equals(socialProvider)) {
            return new GithubOAuth2UserInfo(attributes);
            /*
        } else if (SocialProviderEnum.LINKEDIN.equals(socialProvider)) {
            return new LinkedinOAuth2UserInfo(attributes);
             */
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + socialProvider + " is not supported yet.");
        }
    }
}