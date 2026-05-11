package com.projects.edustore.exception;

public class UserNameAlreadyExistsException extends RuntimeException {

    public UserNameAlreadyExistsException() {
        super("Username already in use");
    }

}
