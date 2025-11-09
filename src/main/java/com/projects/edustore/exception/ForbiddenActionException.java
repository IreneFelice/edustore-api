package com.projects.edustore.exception;



public class ForbiddenActionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ForbiddenActionException() {
        super();
    }

    public ForbiddenActionException(String message) {
        super(message);
    }
}