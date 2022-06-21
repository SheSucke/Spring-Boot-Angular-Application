package eu.profinit.stm.repository.user;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.user.UserMapperMyBatis;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Unit tests for User repository using Mockito
 */

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryImplTest {

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private UserMapperMyBatis userMapperMyBatis;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insertUserThatIsAlreadyInDb() {
        User regUs = new User();
        when(userMapperMyBatis.findUserById(regUs.getEntityId())).thenReturn(regUs);
        Exception ex = Assert.assertThrows(EntityAlreadyExistsException.class, () -> userRepository.insertUser(regUs));
        Assert.assertEquals(new EntityAlreadyExistsException("User").getMessage(), ex.getMessage());
        verify(userMapperMyBatis, times(1)).findUserById(regUs.getEntityId());
        verify(userMapperMyBatis, times(0)).insertUser(regUs);
    }

    @Test
    public void insertUserSuccessfully() throws EntityAlreadyExistsException {
        User regUs = new User();
        when(userMapperMyBatis.findUserById(regUs.getEntityId())).thenReturn(null);
        userRepository.insertUser(regUs);
        verify(userMapperMyBatis, times(1)).findUserById(regUs.getEntityId());
        verify(userMapperMyBatis, times(1)).insertUser(regUs);
    }

    @Test
    public void updateNotExistingUser() {
        User regUs = new User();
        when(userMapperMyBatis.findUserById(regUs.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> userRepository.updateUser(regUs));
        Assert.assertEquals(new EntityNotFoundException("User").getMessage(), ex.getMessage());
        verify(userMapperMyBatis, times(1)).findUserById(regUs.getEntityId());
        verify(userMapperMyBatis, times(0)).updateUser(regUs);
    }

    @Test
    public void updateUser() throws EntityNotFoundException {
        User regUs = new User();
        when(userMapperMyBatis.findUserById(regUs.getEntityId())).thenReturn(regUs);
        userRepository.updateUser(regUs);
        verify(userMapperMyBatis, times(1)).findUserById(regUs.getEntityId());
        verify(userMapperMyBatis, times(1)).updateUser(regUs);
    }

    @Test
    public void findNotExistingUserById() {
        Long userId = 0L;
        when(userMapperMyBatis.findUserById(userId)).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> userRepository.findUserById(userId));
        Assert.assertEquals(new EntityNotFoundException("User").getMessage(), ex.getMessage());
        verify(userMapperMyBatis, times(1)).findUserById(userId);
    }

    @Test
    public void findUserById() throws EntityNotFoundException {
        Long userId = 1L;
        when(userMapperMyBatis.findUserById(userId)).thenReturn(new User());
        User regUs = userRepository.findUserById(userId);
        Assert.assertNotNull(regUs);
        verify(userMapperMyBatis, times(1)).findUserById(userId);
    }

    @Test
    public void findNotExistingUserByEmail() {
        String userEmail = "";
        when(userMapperMyBatis.findUserByEmail(userEmail)).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> userRepository.findUserByEmail(userEmail));
        Assert.assertEquals(new EntityNotFoundException("User").getMessage(), ex.getMessage());
        verify(userMapperMyBatis, times(1)).findUserByEmail(userEmail);
    }

    @Test
    public void findUserByEmail() throws EntityNotFoundException {
        String userEmail = "";
        when(userMapperMyBatis.findUserByEmail(userEmail)).thenReturn(new User());
        User regUs = userRepository.findUserByEmail(userEmail);
        Assert.assertNotNull(regUs);
        verify(userMapperMyBatis, times(1)).findUserByEmail(userEmail);
    }

    @Test
    public void deleteNotExistingUser() {
        Long userId = 0L;
        when(userMapperMyBatis.findUserById(userId)).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> userRepository.deleteUser(userId));
        Assert.assertEquals(new EntityNotFoundException("User").getMessage(), ex.getMessage());
        verify(userMapperMyBatis, times(1)).findUserById(userId);
        verify(userMapperMyBatis, times(0)).deleteUserById(userId);
    }

    @Test
    public void deleteUser() throws EntityNotFoundException {
        Long userId = 1L;
        when(userMapperMyBatis.findUserById(userId)).thenReturn(new User());
        userRepository.deleteUser(userId);
        verify(userMapperMyBatis, times(1)).findUserById(userId);
        verify(userMapperMyBatis, times(1)).deleteUserById(userId);
    }
}
