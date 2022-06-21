package eu.profinit.stm.exception;

/**
 * NonValidUriException is thrown when given URI is not valid.
 */
public class NonValidUriException extends Exception {

    public NonValidUriException() {
        super("URI is not valid!");
    }
}
