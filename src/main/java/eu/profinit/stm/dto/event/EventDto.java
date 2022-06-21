/*
 * EventDto
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.dto.event;

import eu.profinit.stm.adapter.LocalDateTimeAdapter;
import eu.profinit.stm.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * Data transfer object for Event entity. Contains date, place, maxPersonAttendance, CreatedBy and isCanceled.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDto {

    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "date")
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime date;
    @XmlElement(name = "capacity")
    private Integer capacity;
    @XmlElement(name = "isCanceled")
    private boolean isCanceled;
    @XmlElement(name = "place")
    private PlaceDto place;
    @XmlElement(name = "createdBy")
    private UserDto createdBy;


}
