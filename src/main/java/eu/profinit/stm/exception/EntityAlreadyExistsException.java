/*
 * EntityNotFoundException
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.exception;

/**
 * EntityAlreadyExistsException is thrown when entity already exists in some object.
 */
public class EntityAlreadyExistsException extends Exception {

    public EntityAlreadyExistsException(String errorMessage) {
        super(errorMessage + " already exists!");
    }
}
