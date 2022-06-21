/*
 * PlaceDto
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Data transfer object for Event entity. Contains date, place, maxPersonAttendance, CreatedBy and isCanceled.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaceDto {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "address")
    private String address;
    @XmlElement(name = "teamId")
    private Long teamId;
}
