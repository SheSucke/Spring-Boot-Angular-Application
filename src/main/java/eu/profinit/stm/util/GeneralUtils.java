package eu.profinit.stm.util;

import eu.profinit.stm.dto.user.LocalUser;
import eu.profinit.stm.dto.user.UserInfo;
import eu.profinit.stm.model.user.RoleEnum;
import eu.profinit.stm.model.user.SocialProviderEnum;
import eu.profinit.stm.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chinna
 */
public class GeneralUtils {

    public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<RoleEnum> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
                .collect(Collectors.toList());
    }

    public static SocialProviderEnum toSocialProvider(String providerType) {
        return Arrays.stream(SocialProviderEnum.values())
                .filter(provider -> provider.getProviderType().equals(providerType))
                .findFirst().orElse(SocialProviderEnum.LOCAL);
    }

    public static UserInfo buildUserInfo(LocalUser localUser) {
        RoleEnum role = localUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> RoleEnum.valueOf(authority.split("ROLE_")[1]))
                .findFirst().orElse(RoleEnum.NO_ROLE);
        User user = localUser.getUser();
        return new UserInfo(user.getName(), user.getSurname(), user.getEmail(), role);
    }
}
