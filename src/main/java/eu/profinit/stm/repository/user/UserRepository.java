package eu.profinit.stm.repository.user;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.User;
import lombok.NonNull;

public interface UserRepository {

    /**
     * check if user already exists in db
     * @param user user to insert
     */
    void insertUser(@NonNull User user) throws EntityAlreadyExistsException;

    /**
     * find user, if found then update
     * @param user user to update
     * @throws EntityNotFoundException if registeredUser wasn't found
     */
    void updateUser(@NonNull User user) throws EntityNotFoundException;

    /**
     * find user by email, if null then throw
     * @param email to find user by
     * @return found user
     * @throws EntityNotFoundException if user wasn't found
     */
    User findUserByEmail(String email) throws EntityNotFoundException;

    /**
     * find user by id, if null then throw
     * @param id to find user by
     * @return found user
     * @throws EntityNotFoundException if user wasn't found
     */
    User findUserById(Long id) throws EntityNotFoundException;


    /**
     * checks whether user has an id or deleteTeam it by email instead
     * @param userId id to find user to delete
     * @throws EntityNotFoundException user to be deleted wasn't found
     */
    void deleteUser(Long userId) throws EntityNotFoundException;

    Guest insertGuest(Guest guest);
    Guest findGuestByUri(String uri) throws EntityNotFoundException;
    Guest updateGuest (Guest guest);
}
