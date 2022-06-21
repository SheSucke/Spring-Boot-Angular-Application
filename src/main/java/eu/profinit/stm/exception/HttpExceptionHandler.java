/*
 * HttpExceptionHandler
 *
 * 0.1
 *
 * Author: J. Jansky
 */
package eu.profinit.stm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


/**
 * Handling of exceptions provided by the services and setting proper http status.
 */
public class HttpExceptionHandler {
    /**
     * According exception form a service sets http status.
     *
     * @param e exception from a service
     */
    public static void httpErrorMessages(Exception e) {
        if (e.getMessage().equals("Team entity not found!")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().equals("User is not in team")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().equals("Access denied")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } else if (e.getMessage().equals("User is already in subgroup")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().equals("User is already in team")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().equals("User entity not found!")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().equals("Subgroup entity not found!")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().equals("Subgroup already exists")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } else if (e.getMessage().startsWith("Account with e-mail address")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,e.getMessage(),e);
        }
    }
}
