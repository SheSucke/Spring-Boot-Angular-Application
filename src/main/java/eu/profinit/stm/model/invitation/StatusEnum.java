package eu.profinit.stm.model.invitation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    ACCEPTED(1),
    PENDING(2),
    REJECTED(3),
    NO_STATUS(0);

    private final int id;

    public static StatusEnum getId(String input) {
        return Arrays.stream(values())
                .filter(v -> v.getId() == Integer.parseInt(input))
                .findFirst()
                .orElse(NO_STATUS);
    }
}
