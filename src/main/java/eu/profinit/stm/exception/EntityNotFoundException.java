/*
 * EntityNotFoundException
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.exception;

/**
 * EntityNotFoundException is thrown when entity is not found in database.
 */
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String errorMessage) {
        super(errorMessage + " entity not found!");
    }
}
