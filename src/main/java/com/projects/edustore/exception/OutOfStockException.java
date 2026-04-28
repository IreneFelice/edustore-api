package com.projects.edustore.exception;

public class OutOfStockException extends RuntimeException {

    public OutOfStockException() {
        super("Product stock is insufficient");
    }

    public OutOfStockException(String message) {
        super(message);
    }
}

