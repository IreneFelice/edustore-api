package com.projects.edustore.exception;

public class ForbiddenActionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ForbiddenActionException() {

        super("You are not allowed to access or modify this resource");
    }

    public ForbiddenActionException(String message) {
        super(message);
    }
}