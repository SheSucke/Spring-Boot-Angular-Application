package eu.profinit.stm.repository.team;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.team.TeamMapperMyBatis;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.repository.subgroup.SubgroupRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("Main")
public class TeamRepositoryImpl implements TeamRepository {

    @Autowired
    private TeamMapperMyBatis teamMapperMyBatis;

    @Autowired
    private SubgroupRepository subgroupRepository;

    @Override
    public void insertTeam(@NonNull Team team) {
        teamMapperMyBatis.insertTeam(team);
    }

    @Override
    public void deleteTeam(@NonNull Team team) throws EntityNotFoundException {
        checkTeamById(team.getEntityId());
        subgroupRepository.deleteAllTeamSubgroups(team);
        teamMapperMyBatis.deleteTeamById(team.getEntityId());
    }

    @Override
    public void updateTeam(@NonNull Team team) throws EntityNotFoundException {
        checkTeamById(team.getEntityId());
        teamMapperMyBatis.updateTeam(team);
    }

    @Override
    public List<Team> findTeamsByName(String teamName) throws EntityNotFoundException {
        List<Team> teams = teamMapperMyBatis.findTeamsByName(teamName);
        if (teams.isEmpty()) throw new EntityNotFoundException("Team");
        for (Team t : teams) {
            t.setSubgroupList(subgroupRepository.findTeamSubgroups(t));
        }
        return teams;
    }

    @Override
    public Team findTeamById(Long teamId) throws EntityNotFoundException {
        Team team = teamMapperMyBatis.findTeamById(teamId);
        if (team == null) throw new EntityNotFoundException("Team");
        team.setSubgroupList(subgroupRepository.findTeamSubgroups(team));
        return team;
    }

    private void checkTeamById(Long teamId) throws EntityNotFoundException {
        Team team = teamMapperMyBatis.findTeamById(teamId);
        if (team == null) throw new EntityNotFoundException("Team");
    }
}
