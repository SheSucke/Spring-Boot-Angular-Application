/*
 * UserDetailsDTO
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data transfer object for UserDetails entity.
 */
@Data
@XmlRootElement
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class UserDetailsDto {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "password")
    private String password;
    @XmlElement
    private String userName;
    @XmlElement
    private String Surname;

    public UserDetailsDto(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
