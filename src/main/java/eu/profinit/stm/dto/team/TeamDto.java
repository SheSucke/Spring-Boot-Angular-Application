/*
 * TeamDTO
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.dto.team;

import eu.profinit.stm.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Data transfer object for team entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamDto {
    @XmlElement
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "sport")
    private String sport;
    @XmlElement(name = "listOfSubgroups")
    private List<SubgroupDto> listOfSubgroups;
    @XmlElement(name = "owner")
    private UserDto owner;

    /**
     * Obtains a subgroup defined by name from a list of the subgroups.
     *
     * @param subgroupName name of the subgroup
     * @return found subgroup
     */
    public SubgroupDto getTeamSubgroup(String subgroupName) {
        for (SubgroupDto subgroup : listOfSubgroups) {
            if (subgroup.getName().equals(subgroupName)) {
                return subgroup;
            }
        }
        throw new RuntimeException("No subgroup found");
    }
}
