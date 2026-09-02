package com.shoply.backend.common.exception;

public class CredenciaisInvalidasException extends RuntimeException {
    
    public CredenciaisInvalidasException() {
        super("E-mail ou senha inválidos");
    }
}
