package eu.profinit.stm.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class User extends UserParent implements Serializable {
    private String surname;
    private String password;
    private String email;
    private SocialProviderEnum socialProvider;

    public User(String name, String surname, String password, String email, SocialProviderEnum socialProvider) {
        super(name, RoleEnum.USER);
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.socialProvider = socialProvider;
    }

    public User(String name, String surname, String password, String email) {
        super(name, RoleEnum.USER);
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.socialProvider = SocialProviderEnum.LOCAL;
    }
}
