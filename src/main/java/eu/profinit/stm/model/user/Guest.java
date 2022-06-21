package eu.profinit.stm.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class Guest extends UserParent implements Serializable {

    private String uri;

    public Guest(String name, String uri) {
        super(name, RoleEnum.GUEST);
        this.uri = uri;
    }
}
