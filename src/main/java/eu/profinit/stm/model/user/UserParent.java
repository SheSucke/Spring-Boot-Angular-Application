package eu.profinit.stm.model.user;

import eu.profinit.stm.model.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserParent extends Entity {
    private String name;
    private RoleEnum role;
}