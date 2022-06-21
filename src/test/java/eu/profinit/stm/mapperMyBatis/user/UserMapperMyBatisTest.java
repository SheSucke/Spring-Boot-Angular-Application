package eu.profinit.stm.mapperMyBatis.user;/*
 * UserMapperMyBatis
 *
 * 0.1
 *
 * Author: D. Štěpánek
 */

import eu.profinit.stm.configuration.MyBatisConfiguration;
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

/**
 * Unit tests for User mapper
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ContextConfiguration(classes = MyBatisConfiguration.class)
@TestPropertySource("/test.properties")
public class UserMapperMyBatisTest {

    @Autowired
    private UserMapperMyBatis userMapperMyBatis;

    @Test
    public void insertUser() {
        User regUs = new User(
                "Insert",
                "RegUs",
                "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2",
                "insert.regUs@test.com");
        userMapperMyBatis.insertUser(regUs);
        Assert.assertNotNull(regUs.getEntityId());
        Assert.assertNotNull(userMapperMyBatis.findUserById(regUs.getEntityId()));
    }

    @Test
    public void DeleteUserById() {
        Long userId = 1L;
        userMapperMyBatis.deleteUserById(userId);
        Assert.assertNull(userMapperMyBatis.findUserById(userId));
    }

    @Test
    public void DeleteUserByEmail() {
        String userEmail = "delete.byEmail@test.com";
        userMapperMyBatis.deleteUserByEmail(userEmail);
        Assert.assertNull(userMapperMyBatis.findUserByEmail(userEmail));
    }

    @Test
    public void findUserByEmail() {
        String userEmail = "find.byEmail@test.com";
        User foundUser = userMapperMyBatis.findUserByEmail(userEmail);
        Assert.assertNotNull(foundUser);
    }

    @Test
    public void findUserById() {
        Long userId = 4L;
        User foundUser = userMapperMyBatis.findUserById(userId);
        Assert.assertNotNull(foundUser);
    }

    @Test
    public void findNonExistingUserById() {
        Long userId = 0L;
        User regUs = userMapperMyBatis.findUserById(userId);
        Assert.assertNull(regUs);
    }

    @Test
    public void findNonExistingUserByEmail() {
        User regUs = userMapperMyBatis.findUserByEmail("");
        Assert.assertNull(regUs);
    }

    @Test
    public void updateUser() {
        Long userId = 3L;
        User regUs = userMapperMyBatis.findUserById(userId);
        String prevEmail = regUs.getEmail();
        regUs.setEmail("update.regUs@updated.com");
        userMapperMyBatis.updateUser(regUs);
        Assert.assertNotEquals(prevEmail, regUs.getSurname());
    }
}