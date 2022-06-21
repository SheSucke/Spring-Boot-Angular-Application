/*
 * StubUserRepository
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.repository.user;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.logging.Logger;

/**
 * Stub user repository
 */
@Repository
@Profile("stub_repository")
public class UserRepositoryStub implements UserRepository {
    private final Logger logger = Logger.getLogger(String.valueOf(getClass()));
    public static User loggedUser1 = new User("Ivan", "Stastny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@gmail.com");
    private final User loggedUser2 = new User("Pavel", "Smutny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@seznam.cz");
    private final User loggedUser3 = new User("Jirka", "Vesely", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@email.cz");
    private final User loggedUser4 = new User("Tomas", "Smutny", "pass2", "ts@gmail.com");
    public static User loggedUser5 = new User("Adam", "Stastny", "2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "email@gmail.com");
    private final Guest guest = new Guest("Tom","UG1qimsuz2RsW5MrAQM0Wg==");

    @Override
    public void insertUser(User user) {
        logger.info("User saved to database");
        user.setEntityId(10L);
    }

    /**
     * Virtually save user to database
     *
     * @param user saving user
     * @return saved user
     */
    public User createUser(User user) {
        logger.info("User saved to database");
        return user;
    }

    @Override
    public void updateUser(User user) {
        logger.info("User saved to database");
    }

    @Override
    public User findUserById(Long id) throws EntityNotFoundException {
        return new User(
                "Ivan",
                "Stastny",
                "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2",
                "is@gmail.com");
    }

    @Override
    public void deleteUser(Long userId) {

    }

    /**
     * Returns users found by email. Returns two possible users with email address from loggedUser1 up to loggedUser5.
     * Other emails throws EntityNotFoundException.
     *
     * @param userEmail user email
     * @return one of the possible predefined users
     * @throws EntityNotFoundException when email is not equeal to any loggedUser
     */
    @Override
    public User findUserByEmail(String userEmail) throws EntityNotFoundException {
        switch (userEmail) {
        case "is@gmail.com":
            return loggedUser1;
        case "is@seznam.cz":
            return loggedUser2;
        case "is@email.cz":
            return loggedUser3;
        case "ts@gmail.com":
            return loggedUser4;
        case "email@gmail.com":
            return loggedUser5;
        default:
            throw new EntityNotFoundException("User");
        }
    }

    //TODO IMPLEMENT THESE
    @Override
    public Guest insertGuest(Guest guest) {
        guest.setEntityId(1L);
        return guest;
    }

    @Override
    public Guest findGuestByUri(String uri) throws EntityNotFoundException {
        if (uri.equals("UG1qimsuz2RsW5MrAQM0Wg==")) {
            return guest;
        } else if (uri.equals("jsem_place_holder")) {
            return guest;
        } else {
            throw new EntityNotFoundException("Guest");
        }
    }

    @Override
    public Guest updateGuest(Guest guest) {
        return guest;
    }
}
