package eu.profinit.stm.repository.user;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.user.UserMapperMyBatis;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.User;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("Main")
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapperMyBatis userMapperMyBatis;

    private static final String EX_MSG = "User";

    @Override
    public void insertUser(@NonNull User user) throws EntityAlreadyExistsException {
        if (userMapperMyBatis.findUserById(user.getEntityId()) != null) {
            throw new EntityAlreadyExistsException(EX_MSG);
        }
        userMapperMyBatis.insertUser(user);
    }

    @Override
    public void updateUser(@NonNull User user) throws EntityNotFoundException {
        if (userMapperMyBatis.findUserById(user.getEntityId()) == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        userMapperMyBatis.updateUser(user);
    }

    @Override
    public User findUserByEmail(@NonNull String email) throws EntityNotFoundException {
        User foundUser = userMapperMyBatis.findUserByEmail(email);
        if (foundUser == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        return foundUser;
    }

    @Override
    public User findUserById(@NonNull Long id) throws EntityNotFoundException {
        User foundUser = userMapperMyBatis.findUserById(id);
        if (foundUser == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        return foundUser;
    }

    @Override
    public void deleteUser(@NonNull Long userId) throws EntityNotFoundException {
        if (userMapperMyBatis.findUserById(userId) == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        userMapperMyBatis.deleteUserById(userId);
    }

    //TODO IMPLEMENT THESE
    @Override
    public Guest insertGuest(@NonNull Guest guest) {
        return null;
    }

    @Override
    public Guest findGuestByUri(@NonNull String uri) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Guest updateGuest(@NonNull Guest guest) {
        return null;
    }
}
