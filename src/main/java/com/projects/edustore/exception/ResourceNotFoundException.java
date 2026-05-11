package com.projects.edustore.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String subject, Object id) {
        super(subject + " with identifier " + id + " could not be found.");
    }
}


