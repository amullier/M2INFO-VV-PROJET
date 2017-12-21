package fr.istic.vv.mutator.exception;

/**
 * Exception for errors occurred during mutant creation
 */
public class MutatorException extends Exception {

    /**
     * Build an exception with a message
     *
     * @param message
     */
    public MutatorException(String message) {
        super(message);
    }

}
