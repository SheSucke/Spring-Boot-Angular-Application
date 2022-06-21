/*
 * UserIsInSubgroupException
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.exception;

/**
 * Exception is thrown when someone tries to add user who already is a member of subgroup.
 */
public class UserIsInSubgroupException extends RuntimeException {

    public UserIsInSubgroupException(String message) {
        super(message);
    }
}
