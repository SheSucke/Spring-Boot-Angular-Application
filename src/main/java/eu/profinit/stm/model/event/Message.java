/*
 * Message
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.model.event;

import eu.profinit.stm.model.entity.Entity;
import eu.profinit.stm.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class representing Message entity. Contains user, who sent message, message itself, and date when message was send.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message extends Entity implements Serializable {

    private User user;
    private String text;
    private LocalDateTime date;
    private Long eventId;
}
