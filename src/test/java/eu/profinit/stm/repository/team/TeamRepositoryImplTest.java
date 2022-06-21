package eu.profinit.stm.repository.team;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.team.TeamMapperMyBatis;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.repository.subgroup.SubgroupRepositoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test class for team repository, mocked necessary classes
 */
@RunWith(MockitoJUnitRunner.class)
public class TeamRepositoryImplTest {

    @InjectMocks
    private TeamRepositoryImpl teamRepository;

    @Mock
    private TeamMapperMyBatis teamMapperMyBatis;

    @Mock
    private SubgroupRepositoryImpl subgroupRepository;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insertTeam() {
        Team team = new Team();
        teamRepository.insertTeam(team);
        verify(teamMapperMyBatis, times(1)).insertTeam(team);
    }

    @Test
    public void deleteNotExistingTeam() throws EntityNotFoundException {
        Team team = new Team();
        when(teamMapperMyBatis.findTeamById(team.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> teamRepository.deleteTeam(team));
        Assert.assertEquals(new EntityNotFoundException("Team").getMessage(), ex.getMessage());
        verify(teamMapperMyBatis, times(1)).findTeamById(team.getEntityId());
        verify(subgroupRepository, times(0)).deleteAllTeamSubgroups(team);
        verify(teamMapperMyBatis, times(0)).deleteTeamById(team.getEntityId());
    }

    @Test
    public void deleteTeamSuccessfully() throws EntityNotFoundException {
        Team team = new Team();
        when(teamMapperMyBatis.findTeamById(team.getEntityId())).thenReturn(team);
        teamRepository.deleteTeam(team);
        verify(teamMapperMyBatis, times(1)).findTeamById(team.getEntityId());
        verify(subgroupRepository, times(1)).deleteAllTeamSubgroups(team);
        verify(teamMapperMyBatis, times(1)).deleteTeamById(team.getEntityId());
    }

    @Test
    public void updateTeam() throws EntityNotFoundException {
        Team team = new Team();
        when(teamMapperMyBatis.findTeamById(team.getEntityId())).thenReturn(team);
        teamRepository.updateTeam(team);
        verify(teamMapperMyBatis, times(1)).findTeamById(team.getEntityId());
        verify(teamMapperMyBatis, times(1)).updateTeam(team);
    }

    @Test
    public void updateNotExistingTeam() {
        Team team = new Team();
        when(teamMapperMyBatis.findTeamById(team.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> teamRepository.updateTeam(team));
        Assert.assertEquals(new EntityNotFoundException("Team").getMessage(), ex.getMessage());
        verify(teamMapperMyBatis, times(1)).findTeamById(team.getEntityId());
        verify(teamMapperMyBatis, times(0)).updateTeam(team);
    }

    @Test
    public void findTeamsByNameFoundNoTeams() {
        String teamsName = "";
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> teamRepository.findTeamsByName(teamsName));
        Assert.assertEquals(new EntityNotFoundException("Team").getMessage(), ex.getMessage());
        verify(teamMapperMyBatis, times(1)).findTeamsByName(teamsName);
    }

    @Test
    public void findTeamsByName() throws EntityNotFoundException {
        String teamsName = "";
        Team team1 = new Team();
        Team team2 = new Team();
        List<Team> teamList = new ArrayList<>();
        team1.setSport("Sport1");
        team2.setSport("Sport2");
        teamList.add(team1);
        teamList.add(team2);
        when(teamMapperMyBatis.findTeamsByName("")).thenReturn(teamList);
        when(subgroupRepository.findTeamSubgroups(team1)).thenReturn(new ArrayList<>());
        when(subgroupRepository.findTeamSubgroups(team2)).thenReturn(new ArrayList<>());
        teamRepository.findTeamsByName(teamsName);
        verify(subgroupRepository, times(1)).findTeamSubgroups(team1);
        verify(subgroupRepository,  times(1)).findTeamSubgroups(team2);
        verify(teamMapperMyBatis, times(1)).findTeamsByName(teamsName);
    }

    @Test
    public void findNotExistingTeamById() {
        Long teamId = 0L;
        when(teamMapperMyBatis.findTeamById(teamId)).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> teamRepository.findTeamById(teamId));
        Assert.assertEquals(new EntityNotFoundException("Team").getMessage(), ex.getMessage());
        verify(teamMapperMyBatis, times(1)).findTeamById(teamId);
    }

    @Test
    public void findTeamById() throws EntityNotFoundException {
        Long teamId = 1L;
        when(teamMapperMyBatis.findTeamById(teamId)).thenReturn(new Team());
        Team foundTeam = teamRepository.findTeamById(teamId);
        Assert.assertNotNull(foundTeam);
        verify(teamMapperMyBatis, times(1)).findTeamById(teamId);
    }
}
