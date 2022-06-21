/*
 * Place
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.model.event;

import eu.profinit.stm.model.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Class representing Place entity. Contains name and address.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place extends Entity implements Serializable {

    private String name;
    private String address;
    private Long teamId;
}
