package de.modulo.backend.excpetions;

public class InsufficientPermissionsException extends Exception {
    public InsufficientPermissionsException(String message) {
        super(message);
    }
}
