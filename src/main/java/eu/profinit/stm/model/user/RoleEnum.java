package eu.profinit.stm.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN(1),
    USER(2),
    GUEST(3),
    NO_ROLE(0);

    private final int id;

    public static RoleEnum getId(String input) {
        return Arrays.stream(values())
                .filter(v -> v.getId() == Integer.parseInt(input))
                .findFirst()
                .orElse(NO_ROLE);
    }
}
