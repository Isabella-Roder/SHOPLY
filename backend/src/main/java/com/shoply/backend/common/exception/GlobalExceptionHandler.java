package com.shoply.backend.common.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ApiError> tratarUsuarioNaoEncontrado(
        UsuarioNaoEncontradoException exception,
        HttpServletRequest request
    ) {
        return criarResposta(
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
        );
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ApiError> tratarEmailJaCadastrado(
        EmailJaCadastradoException exception,
        HttpServletRequest request
    ) {
        return criarResposta(
            HttpStatus.CONFLICT,
            exception.getMessage(),
            request.getRequestURI(),
            Map.of()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> tratarViolacaoDeIntegridade(
        DataIntegrityViolationException exception,
        HttpServletRequest request
    ) {
        return criarResposta(
            HttpStatus.CONFLICT,
            "Não foi possível concluir a operação por conflito de dados",
            request.getRequestURI(),
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        Map<String, String> campos = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(erro ->
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage())
        );

        return criarResposta(
            HttpStatus.BAD_REQUEST,
            "Dados de entrada inválidos",
            request.getRequestURI(),
            campos
        );
    }

    private ResponseEntity<ApiError> criarResposta(
        HttpStatus status,
        String mensagem,
        String caminho,
        Map<String, String> campos
    ) {
        ApiError erro = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            mensagem,
            caminho,
            campos
        );

        return ResponseEntity.status(status).body(erro);
    }
}
