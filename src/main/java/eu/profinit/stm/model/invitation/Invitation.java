/*
 * Invitation
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.model.invitation;

import eu.profinit.stm.model.entity.Entity;
import eu.profinit.stm.model.user.UserParent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invitation extends Entity implements Serializable {

    private LocalDateTime created;
    private LocalDateTime changed;
    private StatusEnum status;
    private UserParent recipient;
    private Long eventId;
}