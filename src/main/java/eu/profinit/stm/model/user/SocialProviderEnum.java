package eu.profinit.stm.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Chinna
 * @since 26/3/18
 */
@Getter
@AllArgsConstructor
public enum SocialProviderEnum {

	LOCAL(1,"local"),
	GOOGLE(2,"google"),
    FACEBOOK(3,"facebook"),
	GITHUB(4,"github"),
    
    UNSUPPORTED(0, "error");
	//TWITTER("twitter")
	//LINKEDIN("linkedin")

    private final int id;
    private final String providerType;

    public static SocialProviderEnum getId(String input) {
        return Arrays.stream(values())
                .filter(v -> v.getId() == Integer.parseInt(input))
                .findFirst()
                .orElse(UNSUPPORTED);
    }
}
