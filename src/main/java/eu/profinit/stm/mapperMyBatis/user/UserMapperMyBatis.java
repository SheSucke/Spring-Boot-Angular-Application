package eu.profinit.stm.mapperMyBatis.user;

import eu.profinit.stm.model.user.User;

public interface UserMapperMyBatis {

    /**
     *
     * @param email to find RegisteredUser by
     * @return found RegisteredUser or null
     */
    User findUserByEmail(String email);

    /**
     *
     * @param id to find RegisteredUser by
     * @return found RegisteredUser or null
     */
    User findUserById(Long id);

    /**
     * insert RegisteredUser to database and update it's entityId
     * @param user to insert
     */
    void insertUser(User user);

    /**
     *
     * @param id param to find right RegisteredUser to delete
     */
    void deleteUserById(Long id);

    /**
     *
     * @param email param to find right RegisteredUser to delete
     */
    void deleteUserByEmail(String email);

    /**
     * update RegisteredUser in database
     * @param user user to be updated
     */
    void updateUser(User user);
}
