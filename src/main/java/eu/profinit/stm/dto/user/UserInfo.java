package eu.profinit.stm.dto.user;

import eu.profinit.stm.model.user.RoleEnum;
import lombok.Value;

@Value
public class UserInfo {
    String name, surname, email;
    RoleEnum role;
}