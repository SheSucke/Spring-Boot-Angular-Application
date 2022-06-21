package eu.profinit.stm.repository.subgroup;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.subgroup.SubgroupMapperMyBatis;
import eu.profinit.stm.mapperMyBatis.subgroupUser.SubgroupUserMapperMyBatis;
import eu.profinit.stm.model.team.Subgroup;
import eu.profinit.stm.model.team.Team;
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
 * Test class for subgroup repository, mocked necessary classes
 */
@RunWith(MockitoJUnitRunner.class)
public class SubgroupRepositoryImplTest {

    @InjectMocks
    private SubgroupRepositoryImpl subgroupRepository;

    @Mock
    private SubgroupUserMapperMyBatis subgroupUserMapperMyBatis;

    @Mock
    private SubgroupMapperMyBatis subgroupMapperMyBatis;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insertNewSubgroup() throws EntityAlreadyExistsException {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId())).thenReturn(null);
        subgroupRepository.insertSubgroup(subgroup);
        verify(subgroupMapperMyBatis, times(1)).findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId());
        verify(subgroupMapperMyBatis, times(1)).insertSubgroup(subgroup);
    }

    @Test
    public void insertSubgroupThatAlreadyExists() {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId())).thenReturn(subgroup);
        Assert.assertThrows(EntityAlreadyExistsException.class, () -> subgroupRepository.insertSubgroup(subgroup));
        verify(subgroupMapperMyBatis, times(1)).findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId());
        verify(subgroupMapperMyBatis, times(0)).insertSubgroup(subgroup);
    }

    @Test
    public void updateSubgroup() throws EntityNotFoundException, EntityAlreadyExistsException {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupById(subgroup.getEntityId())).thenReturn(subgroup);
        when(subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId())).thenReturn(null);
        subgroupRepository.updateSubgroup(subgroup);
        verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId());
        verify(subgroupMapperMyBatis, times(1)).updateSubgroup(subgroup);
    }

    @Test
    public void updateSubgroupThatDoesNotExist() {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupById(subgroup.getEntityId())).thenReturn(null);
        Assert.assertThrows(EntityNotFoundException.class, () -> subgroupRepository.updateSubgroup(subgroup));
        verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup.getEntityId());
        verify(subgroupMapperMyBatis, times(0)).updateSubgroup(subgroup);
    }

    @Test
    public void updateSubgroupThatAlreadyExistsInATeam() {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupById(subgroup.getEntityId())).thenReturn(subgroup);
        when(subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId())).thenReturn(subgroup);
        Assert.assertThrows(EntityAlreadyExistsException.class, () -> subgroupRepository.updateSubgroup(subgroup));
        verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId());
        verify(subgroupMapperMyBatis, times(0)).updateSubgroup(subgroup);
    }

    @Test
    public void deleteSubgroup() throws EntityNotFoundException {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupById(subgroup.getEntityId())).thenReturn(subgroup);
        subgroupRepository.deleteSubgroup(subgroup);
        verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup.getEntityId());
        verify(subgroupUserMapperMyBatis, times(1)).deleteAllSubgroupUsers(subgroup.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).deleteSubgroupById(subgroup.getEntityId());
    }

    @Test
    public void deleteNotExistingSubgroup() {
        Subgroup subgroup = new Subgroup();
        when(subgroupMapperMyBatis.findSubgroupById(subgroup.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> subgroupRepository.deleteSubgroup(subgroup));
        Assert.assertEquals(new EntityNotFoundException("Subgroup").getMessage(), ex.getMessage());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup.getEntityId());
        verify(subgroupUserMapperMyBatis, times(0)).deleteAllSubgroupUsers(subgroup.getEntityId());
        verify(subgroupMapperMyBatis, times(0)).deleteSubgroupById(subgroup.getEntityId());
    }

    @Test
    public void deleteAllTeamSubgroups() throws EntityNotFoundException {
        Team team = new Team();
        Subgroup subgroup1 = new Subgroup("Sub1", team.getEntityId());
        subgroup1.setEntityId(1L);
        Subgroup subgroup2 = new Subgroup("Sub2", team.getEntityId());
        subgroup1.setEntityId(2L);
        List<Subgroup> subgroupList = new ArrayList<>();
        subgroupList.add(subgroup1);
        subgroupList.add(subgroup2);
        when(subgroupMapperMyBatis.findSubgroupsByTeamId(team.getEntityId())).thenReturn(subgroupList);
        when(subgroupMapperMyBatis.findSubgroupById(subgroup1.getEntityId())).thenReturn(subgroup1);
        when(subgroupMapperMyBatis.findSubgroupById(subgroup2.getEntityId())).thenReturn(subgroup2);
        subgroupRepository.deleteAllTeamSubgroups(team);
         verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup1.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupById(subgroup2.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupsByTeamId(team.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).deleteSubgroupById(subgroup1.getEntityId());
        verify(subgroupMapperMyBatis, times(1)).deleteSubgroupById(subgroup2.getEntityId());
    }

    @Test
    public void findTeamSubgroupByName() throws EntityNotFoundException {
        Subgroup subgroup = new Subgroup();
        Team team = new Team();
        when(subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId())).thenReturn(subgroup);
        subgroupRepository.findTeamSubgroupByName(team, subgroup.getName());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId());
        verify(subgroupUserMapperMyBatis, times(1)).findUsersBySubgroupId(subgroup.getEntityId());
    }

    @Test
    public void findNotExistingTeamSubgroupByName() {
        Subgroup subgroup = new Subgroup();
        Team team = new Team();
        when(subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> subgroupRepository.findTeamSubgroupByName(team, subgroup.getName()));
        Assert.assertEquals(new EntityNotFoundException("Subgroup").getMessage(), ex.getMessage());
        verify(subgroupMapperMyBatis, times(1)).findSubgroupByNameAndTeamId(subgroup.getName(), subgroup.getTeamId());
        verify(subgroupUserMapperMyBatis, times(0)).findUsersBySubgroupId(subgroup.getEntityId());
    }

    @Test
    public void findTeamSubgroups() {
        Team team = new Team();
        Subgroup subgroup = new Subgroup();
        List<Subgroup> subgroupList = new ArrayList<>();
        subgroupList.add(subgroup);
        team.setSubgroupList(subgroupList);
        when(subgroupMapperMyBatis.findSubgroupsByTeamId(team.getEntityId())).thenReturn(team.getSubgroupList());
        subgroupRepository.findTeamSubgroups(team);
        verify(subgroupMapperMyBatis, times(1)).findSubgroupsByTeamId(team.getEntityId());
        verify(subgroupUserMapperMyBatis, times(1)).findUsersBySubgroupId(subgroup.getEntityId());
    }
}