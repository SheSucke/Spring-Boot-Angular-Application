package eu.profinit.stm.mapperMyBatis.subgroup;

import eu.profinit.stm.model.team.Subgroup;

import java.util.List;

public interface SubgroupMapperMyBatis {

    /**
     * insert Subgroup to database and update it's entityId
     * @param subgroup to insert
     */
    void insertSubgroup(Subgroup subgroup);

    /**
     *
     * @param subgroup to be updated
     */
    void updateSubgroup(Subgroup subgroup);

    /**
     *
     * @param id to delete subgroup by
     */
    void deleteSubgroupById(Long id);

    /**
     *
     * @param id to find subgroup by
     * @return found subgroup or null
     */
    Subgroup findSubgroupById(Long id);

    /**
     *
     * @param subgroupName name of a desired subgroup
     * @param teamId id of a parent team
     * @return found subgroup or null
     */
    Subgroup findSubgroupByNameAndTeamId(String subgroupName, Long teamId);

    /**
     *
     * @param teamId to find subgroups by
     * @return list of all subgroups that belongs to team
     */
    List<Subgroup> findSubgroupsByTeamId(Long teamId);
}
