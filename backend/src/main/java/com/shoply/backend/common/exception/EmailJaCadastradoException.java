package com.shoply.backend.common.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException() {
        super("E-mail já cadastrado");
    }
}
