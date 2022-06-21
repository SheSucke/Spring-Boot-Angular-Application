package eu.profinit.stm.mapperMyBatis.subgroup;

import eu.profinit.stm.configuration.MyBatisConfiguration;
import eu.profinit.stm.model.team.Subgroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ContextConfiguration(classes = MyBatisConfiguration.class)
@TestPropertySource("/test.properties")
public class SubgroupMapperMyBatisTest {

    @Autowired
    private SubgroupMapperMyBatis subgroupMapperMyBatis;

    @Test
    public void insertSubgroup() {
        Long teamId = 5L;
        Subgroup subgroup = new Subgroup("Test subgroup", teamId);
        subgroupMapperMyBatis.insertSubgroup(subgroup);
        Assert.assertNotNull(subgroup.getEntityId());
        Assert.assertNotNull(subgroupMapperMyBatis.findSubgroupById(subgroup.getEntityId()));
    }

    @Test
    public void insertSubgroupThatAlreadyExists() {
        Long teamId = 5L;
        String subgroupName = "New subgroup";
        Subgroup subgroup1 = new Subgroup(subgroupName, teamId);
        Subgroup subgroup2 = new Subgroup(subgroupName, teamId);
        subgroupMapperMyBatis.insertSubgroup(subgroup1);
        Assert.assertThrows(DuplicateKeyException.class, () -> subgroupMapperMyBatis.insertSubgroup(subgroup2));
    }

    @Test
    public void updateSubgroupName() {
        Long subgroupId = 1L;
        Subgroup subgroup = subgroupMapperMyBatis.findSubgroupById(subgroupId);
        String prevName = subgroup.getName();
        subgroup.setName("New Name group");
        subgroupMapperMyBatis.updateSubgroup(subgroup);
        Assert.assertNotEquals(prevName, subgroup.getName());
    }

    @Test
    public void deleteSubgroupById() {
        Long subgroupId = 2L;
        subgroupMapperMyBatis.deleteSubgroupById(subgroupId);
        Assert.assertNull(subgroupMapperMyBatis.findSubgroupById(subgroupId));
    }

    @Test
    public void findSubgroupById() {
        Long subgroupId = 3L;
        Subgroup subgroup = subgroupMapperMyBatis.findSubgroupById(subgroupId);
        Assert.assertNotNull(subgroup);
    }

    @Test
    public void findSubgroupsByTeamId() {
        Long teamId = 6L;
        List<Subgroup> subgroupList = subgroupMapperMyBatis.findSubgroupsByTeamId(teamId);
        Assert.assertEquals(3, subgroupList.size());
    }

    @Test
    public void findSubgroupByNameAndTeamId() {
        Long teamId = 6L;
        String subgroupName = "Find subgroup by name and teamId";
        Subgroup foundSubgroup = subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroupName, teamId);
        Assert.assertNotNull(foundSubgroup);
        Assert.assertEquals(subgroupName, foundSubgroup.getName());
    }

    @Test
    public void findNotExistingSubgroupByNameAndTeamId() {
        Long teamId = 0L;
        String subgroupName = "";
        Subgroup foundSubgroup = subgroupMapperMyBatis.findSubgroupByNameAndTeamId(subgroupName, teamId);
        Assert.assertNull(foundSubgroup);
    }
}
