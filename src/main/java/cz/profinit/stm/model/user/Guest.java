package cz.profinit.stm.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class Guest extends User {

    private String uri;

    public Guest(String name, String uri) {
        super(name, RoleEnum.GUEST);
        this.uri = uri;
    }
}