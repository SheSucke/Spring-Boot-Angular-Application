/*
 * RegisteredUserDTO
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data transfer object for Registered user entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto {

    @NotNull
    @XmlElement(name = "name")
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;
}
