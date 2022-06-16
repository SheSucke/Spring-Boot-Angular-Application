/*
 * MessageDto
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package cz.profinit.stm.dto.event;

import cz.profinit.stm.adapter.LocalDateTimeAdapter;
import cz.profinit.stm.dto.user.RegisteredUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;


/**
 * Data transfer object represents Message entity. Contains user, message and date.
 */
@Data
@AllArgsConstructor
public class MessageDto {

    @XmlElement(name = "user")
    private final RegisteredUserDto user;
    @XmlElement(name = "text")
    private final String text;
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private final LocalDateTime date;
    @XmlElement(name = "eventId")
    private final Long eventId;

}