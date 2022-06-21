package eu.profinit.stm.mapperMyBatis.subgroupUser;

import eu.profinit.stm.configuration.MyBatisConfiguration;
import eu.profinit.stm.mapperMyBatis.subgroup.SubgroupMapperMyBatis;
import eu.profinit.stm.mapperMyBatis.user.UserMapperMyBatis;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ContextConfiguration(classes = MyBatisConfiguration.class)
@TestPropertySource("/test.properties")
public class SubgroupUserMapperMyBatisTest {

    @Autowired
    private SubgroupUserMapperMyBatis subgroupUserMapperMyBatis;

    @Autowired
    private SubgroupMapperMyBatis subgroupMapperMyBatis;

    @Autowired
    private UserMapperMyBatis userMapperMyBatis;

    @Test
    public void insertUserToSubgroup() {
        Long subgroupId = 6L;
        Long userId = 8L;
        Assert.assertEquals(0, subgroupUserMapperMyBatis.findUsersBySubgroupId(subgroupId).size());
        subgroupUserMapperMyBatis.insertSubgroupUser(subgroupId, userId);
        Assert.assertEquals(1, subgroupUserMapperMyBatis.findUsersBySubgroupId(subgroupId).size());
    }

    @Test
    public void deleteUserFromSubgroup() {
        Long subgroupId = 6L;
        Long userId = 9L;
        Assert.assertEquals(1, subgroupUserMapperMyBatis.findUsersBySubgroupId(subgroupId).size());
        subgroupUserMapperMyBatis.deleteSubgroupUser(subgroupId, userId);
        Assert.assertEquals(0, subgroupUserMapperMyBatis.findUsersBySubgroupId(subgroupId).size());
    }

    @Test
    public void findUsersBySubgroupId() {
        Long subgroupId = 7L;
        List<User> userList = subgroupUserMapperMyBatis.findUsersBySubgroupId(subgroupId);
        Assert.assertEquals(2, userList.size());
        userList.forEach(Assert::assertNotNull);
    }
}