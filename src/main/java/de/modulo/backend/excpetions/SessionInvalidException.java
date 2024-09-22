package de.modulo.backend.excpetions;

public class SessionInvalidException extends RuntimeException {
    public SessionInvalidException(String message) {
        super(message);
    }
}
