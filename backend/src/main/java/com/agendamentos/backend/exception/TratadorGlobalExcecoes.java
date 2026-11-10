package com.agendamentos.backend.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorGlobalExcecoes {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException excecao) {

        Map<String, String> erros = excecao.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        erro -> erro.getField(),
                        erro -> erro.getDefaultMessage() == null
                                ? "Valor inválido"
                                : erro.getDefaultMessage(),
                        (primeiro, segundo) -> primeiro));

        return ResponseEntity.badRequest().body(
                Map.of(
                        "mensagem", "Dados inválidos.",
                        "erros", erros));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarArgumentoInvalido(
            IllegalArgumentException excecao) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("mensagem", excecao.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarIntegridade(
            DataIntegrityViolationException excecao) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("mensagem", "Não foi possível concluir o cadastro devido a um conflito de dados."));
    }
}